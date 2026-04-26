/*
 * Copyright (c) 2026 Fujitsu Limited. All rights reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.fujitsu.demo;

import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.service.tool.ToolProvider;
import dev.langchain4j.agentic.supervisor.SupervisorResponseStrategy;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

public class SupervisorAgentFactory {
  
  @Inject
  @ConfigProperty(name = "model.api.key")
  private String modelKey;

  @Inject
  @ConfigProperty(name = "model.name")
  private String modelName;

  @Inject
  BankAgent.CreditAgent creditAgent;

  @Inject
  BankAgent.WithdrawAgent withdrawAgent;
  
  @Inject
  BankAgent.RollbackAgent rollbackAgent;


  @Produces
  @RequestScoped
  public SupervisorAgent createSuperVisorAgent() {

    var model = GoogleAiGeminiChatModel.builder()
         .apiKey(modelKey)
         .modelName(modelName)
         .logRequestsAndResponses(true)
         .build();
    var agent = AgenticServices.supervisorBuilder(SupervisorAgent.class)
      .subAgents(creditAgent, withdrawAgent, rollbackAgent)
      .chatModel(model)
      // you need to tune following supervisor context depends on using model
      .supervisorContext("""
          Highest Priority Policies:
            After a 'rollback-agent' is called, do not call 'credit-agent'.
            After a 'rollback-agent' is called, call 'done' method to complete the process even if requests remain.

          Other Policies: 
            Call 'credit-agent' for the request to credit,
            and call 'withdraw-agent' for the request to withdraw.
		        If he user's request includes multiple actions (e.g., both withdraw and credit), 
            define them as separate steps.
		        Do not complete the process until all steps have been finished.
            If one of the actions failed, do not try the same action again,
            instead, must call 'rollback-agent' for each transaction.
        """)
      .responseStrategy(SupervisorResponseStrategy.SUMMARY)
      .build();

    return agent;
  }

}
