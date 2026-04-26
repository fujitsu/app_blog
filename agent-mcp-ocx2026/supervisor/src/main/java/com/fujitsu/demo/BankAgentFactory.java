/*
 * Copyright (c) 2026 Fujitsu Limited. All rights reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.fujitsu.demo;

import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.service.tool.ToolProvider;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.http.StreamableHttpMcpTransport;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.openid.OpenIdContext;

import java.time.Duration;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class BankAgentFactory {
  
  @Inject
  @ConfigProperty(name = "mcp.bank.url")
  private String mcpBankUrl;
  @Inject
  @ConfigProperty(name = "model.api.key")
  private String modelKey;
  @Inject
  @ConfigProperty(name = "model.name")
  private String modelName;

  @Inject
  private OpenIdContext context;

  @Inject
  private McpClient mcpClient;

  @Inject
  private ToolProvider provider;
  
  @Produces
  @ApplicationScoped
  public McpClient createMcpClient() {
    var bearer = "Bearer " + context.getIdentityToken();

    StreamableHttpMcpTransport transport = StreamableHttpMcpTransport.builder()
      .url(mcpBankUrl)
      .customHeaders(Map.of("Authorization", bearer))
      .timeout(Duration.ofSeconds(60))
      .logRequests(true)
      .logResponses(true)
      .build();
    McpClient mcpClient = new DefaultMcpClient.Builder()
      .transport(transport)
      .build();
    return mcpClient;
  }

  @Produces
  @ApplicationScoped
  public ToolProvider createToolProvider() {
    ToolProvider provider = McpToolProvider.builder()
      .mcpClients(List.of(mcpClient))
      .build();
    return provider;
  }

  @Produces
  @RequestScoped
  public BankAgent.CreditAgent createCreditAgent() {
    var model = GoogleAiGeminiChatModel.builder()
          .apiKey(modelKey)
          .modelName(modelName)
          .logRequestsAndResponses(true)
          .build();
    var agent = AgenticServices
          .agentBuilder(BankAgent.CreditAgent.class)
          .chatModel(model)
          .toolProvider(provider)
          .build();
    return agent;
  }

  @Produces
  @RequestScoped
  public BankAgent.WithdrawAgent createWithdrawAgent() {
    var model = GoogleAiGeminiChatModel.builder()
          .apiKey(modelKey)
          .modelName(modelName)
          .logRequestsAndResponses(true)
          .build();
    var agent = AgenticServices
          .agentBuilder(BankAgent.WithdrawAgent.class)
          .chatModel(model)
          .toolProvider(provider)
      .build();
    return agent;
  }


  @Produces
  @RequestScoped
  public BankAgent.RollbackAgent createRollbackAgent() {
    var model = GoogleAiGeminiChatModel.builder()
          .apiKey(modelKey)
          .modelName(modelName)
          .logRequestsAndResponses(true)
          .build();
    var agent = AgenticServices
          .agentBuilder(BankAgent.RollbackAgent.class)
          .chatModel(model)
          .toolProvider(provider)
          .build();
    return agent;
  }

}
