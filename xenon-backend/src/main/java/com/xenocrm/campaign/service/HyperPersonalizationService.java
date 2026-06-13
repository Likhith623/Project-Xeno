package com.xenocrm.campaign.service;

import com.xenocrm.agent.service.AgentLlmGatewayService;
import com.xenocrm.customer.entity.CustomerEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class HyperPersonalizationService {

    private final AgentLlmGatewayService llmGatewayService;
    private final DynamicDiscountService dynamicDiscountService;

    public String generatePersonalizedBody(String baseBodyHtml, CustomerEntity customer) {
        String discountTier = dynamicDiscountService.calculateDiscountTier(customer);

        if (customer.getCustomAttributes() == null || customer.getCustomAttributes().isEmpty()) {
            return baseBodyHtml.replace("{{discount}}", discountTier);
        }

        String prompt = "You are a Hyper-Personalization engine. Here is the base email body:\n" +
                        baseBodyHtml + "\n\n" +
                        "Here is the customer profile:\n" +
                        "Name: " + customer.getName() + "\n" +
                        "Attributes: " + customer.getCustomAttributes().toString() + "\n" +
                        "Discount To Offer: " + discountTier + "\n\n" +
                        "Rewrite the base email body to perfectly personalize it for this customer based on their attributes and seamlessly weave in the assigned discount offer. " +
                        "Keep the HTML formatting exactly the same. Respond ONLY with the modified HTML.";

        try {
            String llmResponse = llmGatewayService.callGemini(prompt);
            if (llmResponse.startsWith("```html")) llmResponse = llmResponse.substring(7);
            if (llmResponse.startsWith("```")) llmResponse = llmResponse.substring(3);
            if (llmResponse.endsWith("```")) llmResponse = llmResponse.substring(0, llmResponse.length() - 3);
            return llmResponse.trim();
        } catch (Exception e) {
            log.warn("Hyper-personalization failed for customer {}, falling back to base body.", customer.getId(), e);
            return baseBodyHtml;
        }
    }
}
