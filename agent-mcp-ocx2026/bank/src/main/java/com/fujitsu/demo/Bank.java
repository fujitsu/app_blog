/*
 * Copyright (c) 2026 Fujitsu Limited. All rights reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.fujitsu.demo;

import java.util.UUID;
import java.util.HashMap;
import java.util.Map;
import jakarta.annotation.Resource;
import jakarta.transaction.UserTransaction;
import javax.naming.InitialContext;

public class Bank {
  // use only two accounts for demo
  private static Map<String, Account> accountMap = 
    Map.of("Alice", new Account("Alice", 100),
           "Bob", new Account("Bob", 200));

  public static class Account {
    String accountName;
    int balance;
    public Account(String accountName, int balance) {
      this.accountName = accountName;
      this.balance = balance;
    }
    public String getAccountName() { return accountName;}
    public void setAccountName(String name) { this.accountName = name;}
    public int getBalance() { return balance;}
    public void setBalance(int balance) { this.balance = balance;}
  } 

  // mapping of trnsaction id(String) and transaction object(Bank.Transaction.class)
  static private HashMap<String, Transaction> transactionMap = new HashMap<>();

  public static String createTransaction(
            String accountName, Transaction.Operation op, int value) {
    var transactionId = UUID.randomUUID().toString();
    var transaction = new Transaction(transactionId, accountName, op, value);
    transactionMap.put(transactionId, transaction);
    return transactionId;
  }

  public static boolean processTransaction(String transactionId) {
    var transaction = transactionMap.get(transactionId);
    var done = transaction.processTransaction();
    if (done)
      transactionMap.remove(transactionId);
    return done;
  }


  public static class Transaction {
    private String transactionId;
    private String accountName;
    private int value;
    private Operation op;

    enum Operation {CREDIT, WITHDRAW};
  
    public Transaction(String transactionId, String accountName, Operation op, int value) {
      this.transactionId = transactionId;
      this.accountName = accountName;
      this.value = value;
      this.op = op;
    }

    @Resource
    private UserTransaction utx;
	
    /**
      simulate local transactions using Jakarta Transaction
    */
    public boolean processTransaction() {
      try {
        utx = (UserTransaction)new InitialContext().lookup("java:comp/UserTransaction");	  
        utx.begin();
        Account ac = accountMap.get(accountName);
        switch (op) {
          case Operation.CREDIT:
            ac.setBalance(ac.getBalance()+value);
            break;
          case Operation.WITHDRAW:
            if (ac.getBalance() >= value)
              ac.setBalance(ac.getBalance()-value);
            else {
              utx.rollback();
              return false;
            }
        }
        utx.commit();

        return true;
      } catch (Exception e) {
        e.printStackTrace();
        try {
          utx.rollback();
        } catch (Exception r) {
        }
        return false;
      }
    }
  } 

  static boolean rollback(String transactionId) {
    // empty for demo
    return true;
  }
  
}

