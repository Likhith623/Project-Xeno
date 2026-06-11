package com.xenocrm.agent.service.tools;

import org.springframework.stereotype.Component;

@Component
public class FetchCustomerMetricsTool implements AgentTool {

    @Override
    public String getName() {
        return "fetch_customer_metrics";
    }

    @Override
    public String getDescription() {
        return "Fetches metrics and CLV for a specific customer or segment.";
    }

    @Override
    public String execute(String parametersJson) {
        return "Metrics fetched based on parameters.";
    }
}
