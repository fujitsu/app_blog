/*
 * Copyright (c) 2026 Fujitsu Limited. All rights reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.fujitsu.demo;

import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpStatelessServerFeatures;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.HttpServletStatelessServerTransport;
import io.modelcontextprotocol.json.McpJsonDefaults;
import io.modelcontextprotocol.json.McpJsonMapper;
import io.modelcontextprotocol.spec.McpSchema;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import jakarta.enterprise.context.Initialized;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.HttpConstraintElement;
import jakarta.servlet.ServletSecurityElement;
import jakarta.servlet.annotation.ServletSecurity.EmptyRoleSemantic;
import jakarta.servlet.annotation.ServletSecurity.TransportGuarantee;

@WebListener
public class MCPServletProvider implements ServletContextListener {

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    IO.println("creating mcp servlet ... ");
 
    var mcpServlet = HttpServletStatelessServerTransport
            .builder()
            .messageEndpoint("/mcp")
            .build();
    var syncServer = McpServer.sync(mcpServlet)
            .serverInfo("banker-server", "1.0.0")
            .capabilities(McpSchema.ServerCapabilities.builder()
                            .tools(true)
                            .resources(false, false)
                            .build())
            .build();

    syncServer.addTool(createCreditTool());
    syncServer.addTool(createWithdrawTool());
    syncServer.addTool(createRollbackTool());

    // add servlet instance to container
    ServletContext context = sce.getServletContext();
    ServletRegistration.Dynamic registration = context.addServlet("MCPServlet", mcpServlet);
    registration.addMapping("/mcp");

    // set security constraint to use JWT
    HttpConstraintElement httpConstraint =
      new HttpConstraintElement(EmptyRoleSemantic.PERMIT, 
                                TransportGuarantee.NONE, 
                                new String[]{"banker"});
    ServletSecurityElement securityElement = new ServletSecurityElement(httpConstraint);
    registration.setServletSecurity(securityElement);

  }
 
  private McpStatelessServerFeatures.SyncToolSpecification createCreditTool() {
    var toolspec = McpStatelessServerFeatures.SyncToolSpecification.builder()
      .tool(McpSchema.Tool.builder()
        .name("credit-transaction")
        .description("""
            We will credit the amount to the specified account.
            We will provide the transaction id to be used in subsequent requests.
          """)
        .inputSchema(McpJsonDefaults.getMapper(),
          """
            {
              "type": "object",
              "properties": {
                 "account-name": {"type": "string"},
                 "amount":  {"type": "number"}
              },
              "required": ["account-name", "amount"]
            }
          """)
        .build())
      .callHandler( (exchange, req) -> {
         IO.println("incoming request: " + req);	
         String accountName = (String)req.arguments().get("account-name");
         int amount = ((Number)req.arguments().get("amount")).intValue();
         var transactionId = Bank.createTransaction(accountName, Bank.Transaction.Operation.CREDIT, amount);		  
         boolean done = Bank.processTransaction(transactionId);			
         if (done)
           return McpSchema.CallToolResult.builder()
	           .content(List.of(new McpSchema.TextContent("""
                 Transaction ID is %s. The transaction has completed.
               """.formatted(transactionId))))
             .isError(false)
             .build();
         return McpSchema.CallToolResult.builder()
            .content(List.of(new McpSchema.TextContent("""
	       Transaction ID is %s. The transaction failed.
               """.formatted(transactionId))))
            .isError(true)
            .build();
          })
          .build();

     return toolspec;
  }

  private McpStatelessServerFeatures.SyncToolSpecification createWithdrawTool() {
    var toolspec = McpStatelessServerFeatures.SyncToolSpecification.builder()
          .tool(McpSchema.Tool.builder()
          .name("withdraw-transaction")
          .description("""
		        We will withdraw the amount from the specified account id.
			      We will provide the transaction id to be used in subsequent requests.
		       """)
          .inputSchema(McpJsonDefaults.getMapper(),
             """
               {
                 "type": "object",
                 "properties": {
                    "account-name": {"type": "string"},
                    "amount":  {"type": "number"}
                  },
                  "required": ["account-name", "amount"]
                }
              """)
            .build())
          .callHandler( (exchange, req) -> {

            IO.println("incoming request: " + req);

            String accountName = (String)req.arguments().get("account-name");
            int amount = ((Number)req.arguments().get("amount")).intValue();
            var transactionId = Bank.createTransaction(accountName, Bank.Transaction.Operation.WITHDRAW, amount);
            boolean done = Bank.processTransaction(transactionId);

            if (done)
              return McpSchema.CallToolResult.builder()
	            .content(List.of(new McpSchema.TextContent("""
				    Transaction ID is %s. The transaction has completed.
                  """.formatted(transactionId))))
                .isError(false)
                .build();
            return McpSchema.CallToolResult.builder()
              .content(List.of(new McpSchema.TextContent("""
	             Transaction ID is %s. The transaction failed.
                """.formatted(transactionId))))
              .isError(true)
              .build();
          })
          .build();

	return toolspec;
  }

  private McpStatelessServerFeatures.SyncToolSpecification createRollbackTool() {
    var toolspec = McpStatelessServerFeatures.SyncToolSpecification.builder()
          .tool(McpSchema.Tool.builder()
          .name("rollback-transaction")
          .description("We will rollback the specified transaction.")
          .inputSchema(McpJsonDefaults.getMapper(),
             """
               {
                 "type": "object",
                 "properties": {
                    "transaction-id": {"type": "string"}
                  },
                  "required": ["transaction-id"]
                }
              """)
            .build())
          .callHandler( (exchange, req) -> {

            IO.println("incoming request: " + req);

            String transactionId = (String)req.arguments().get("transaction-id");
            boolean success = Bank.rollback(transactionId);
            if (success)
              return McpSchema.CallToolResult.builder()
                .content(List.of(new McpSchema.TextContent("Rollback of " + transactionId + "has completed.")))
                .isError(false)
                .build();
            else
              return McpSchema.CallToolResult.builder()
                .content(List.of(new McpSchema.TextContent("Rollback of " + transactionId + " failed.")))
                .isError(true)
                .build();
          })
          .build();

	return toolspec;
  }

  
}

