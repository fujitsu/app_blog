/*
 * Copyright (c) 2026 Fujitsu Limited. All rights reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.fujitsu.demo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.annotation.Priority;
import jakarta.interceptor.Interceptor;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.identitystore.IdentityStore;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.CredentialValidationResult.Status;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;
import jakarta.enterprise.inject.Instance;
import java.util.Set;
import java.util.HashSet;


@Alternative 
@Priority(Interceptor.Priority.APPLICATION)
@ApplicationScoped
public class SecureHandler implements IdentityStoreHandler {
  @Inject
  Instance<IdentityStore> identityStores; 

  @Override
  public CredentialValidationResult validate(Credential credential) {
    CredentialValidationResult result = null;
    Set<String> groups = new HashSet<>();

    for (IdentityStore identityStore : identityStores) {
      result = identityStore.validate(credential);
      if (result.getStatus() == Status.NOT_VALIDATED) {
        continue;
      }

      if (result.getStatus() == Status.INVALID) {
        return CredentialValidationResult.INVALID_RESULT;
      }

      groups.addAll(result.getCallerGroups());
    }

    return new CredentialValidationResult(result.getCallerPrincipal(), groups);
  }

}

