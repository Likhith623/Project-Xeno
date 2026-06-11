package com.xenocrm.agent.service.tools;

/**
 * AgentTool — Common interface for all tools the Sovereign Agent can use.
 */
public interface AgentTool {
    String getName();
    String getDescription();
    String execute(String parametersJson);
}
