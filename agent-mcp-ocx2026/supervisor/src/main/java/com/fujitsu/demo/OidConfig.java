/*
 * Copyright (c) 2026 Fujitsu Limited. All rights reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.fujitsu.demo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Configuration for OpenID Connect
 */
@ApplicationScoped
@Named("oidConfig")
public class OidConfig {

  @Inject
  @ConfigProperty(name = "oid.redirectUrl")
  private String redirectUrl;

  @Inject
  @ConfigProperty(name = "oid.providerUrl")
  private String providerUrl;

  @Inject
  @ConfigProperty(name = "oid.clientId")
  private String clientId;

  @Inject
  @ConfigProperty(name = "oid.clientSecret")
  private String clientSecret;

  public String getRedirectUrl() { return redirectUrl; }

  public String getProviderUrl() { return providerUrl; }

  public String getClientId() { return clientId; }

  public String getClientSecret() { return clientSecret; }
}

