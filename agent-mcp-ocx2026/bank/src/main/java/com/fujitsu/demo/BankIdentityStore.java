/*
 * Copyright (c) 2026 Fujitsu Limited. All rights reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.fujitsu.demo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.identitystore.IdentityStore;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.credential.Credential;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import java.util.Set;
import java.util.EnumSet;

@ApplicationScoped
public class BankIdentityStore implements IdentityStore {

  @Override
  public Set<ValidationType> validationTypes() {
    return EnumSet.of(ValidationType.PROVIDE_GROUPS);
  }

  @Inject
  @ConfigProperty(name = "bank.userid")
  private String userid;
  
  @Override
  public Set<String> getCallerGroups(final CredentialValidationResult validationResult) {
    if (userid.equals(validationResult.getCallerPrincipal().getName()))
      return Set.of("banker");
    return Set.of("");
  }
}

