/*
 * Copyright (c) 2026 Fujitsu Limited. All rights reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.fujitsu.demo;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface BankAgent {

  public static interface CreditAgent {
    @Agent(description="""
            Executes a credit transaction. 
			      Input: 'amount' (number) and 'account-name' (string).
			      Use this tool whenever a deposit, or credit to an account is requested. 
			      Returns a unique transaction ID. 
           """,
           name = "credit-agent")
    @UserMessage("""
        Please credit the {{amount}} euros to the account {{account-name}}.
        The response must include the id of this transaction.
      """)
    String credit(@V("account-name") String accountId, @V("amount") int amount);
  }

  public static interface WithdrawAgent {
    @Agent(description="""
             Executes a withdrawal transaction. 
			       Input: 'amount' (number) and 'account-name' (string). 
			       Use this tool whenever money needs to be taken out or debited from an account. 
			       Returns a unique transaction ID.
           """,
           name = "withdraw-agent")
    @UserMessage("""
        Please withdraw the {{amount}} euros from the account {{account-name}}.
        The response must include the id of this transaction.
      """)
    String withdraw(@V("account-name") String accountId, @V("amount") int amount);
  }

  public static interface RollbackAgent {
    @Agent(description="""
             We will rollback the transaction sepcified by the transaction-id.
           """,
           name = "rollback-agent")
    @UserMessage("""
         Please rollback the transaction {{transaction-id}}. 
       """)
    String rollback(@V("transaction-id") String transactionId);
  }

}

