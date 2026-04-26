/*
 * Copyright (c) 2026 Fujitsu Limited. All rights reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.fujitsu.demo;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptor;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanismHandler;
import jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.glassfish.api.security.jwt.MicroProfileJwtAuthenticationMechanism;

@BasicAuthenticationMechanismDefinition
@ApplicationScoped
@Alternative
@Priority(Interceptor.Priority.APPLICATION)
public class JWTAuthHandler implements HttpAuthenticationMechanismHandler {

  @Inject
  @MicroProfileJwtAuthenticationMechanism
  HttpAuthenticationMechanism jwtAuthentication;

  @Inject
  @BasicAuthenticationMechanismDefinition.BasicAuthenticationMechanism
  HttpAuthenticationMechanism basicAuthentication;

  @Override
  public AuthenticationStatus validateRequest(
          HttpServletRequest request,
          HttpServletResponse response,
          HttpMessageContext messageContext) throws AuthenticationException {
    return jwtAuthentication.validateRequest(request, response, messageContext);
  }

}
