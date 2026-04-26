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

import java.util.Set;
import java.util.EnumSet;

@ApplicationScoped
public class SecureIdentityStore implements IdentityStore {

  @Override
  public CredentialValidationResult validate(Credential credential) {
    return new CredentialValidationResult("bank", Set.of("supervisor"));
  }


  @Override
  public Set<ValidationType> validationTypes() {
    return EnumSet.of(ValidationType.PROVIDE_GROUPS);
  }

  @Override
  public Set<String> getCallerGroups(final CredentialValidationResult validationResult) {
    return Set.of("supervisor");
  }
}

