package com.xenocrm.agent.service.tools;

import org.springframework.stereotype.Component;

@Component
public class GenerateCampaignTool implements AgentTool {

    @Override
    public String getName() {
        return "generate_campaign";
    }

    @Override
    public String getDescription() {
        return "Generates a new campaign with multiple variants based on a prompt.";
    }

    @Override
    public String execute(String parametersJson) {
        return "Campaign generated based on parameters.";
    }
}
