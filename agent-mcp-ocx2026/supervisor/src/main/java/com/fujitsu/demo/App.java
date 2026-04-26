package com.fujitsu.demo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import jakarta.security.enterprise.authentication.mechanism.http.OpenIdAuthenticationMechanismDefinition;
import jakarta.annotation.security.DeclareRoles;


@OpenIdAuthenticationMechanismDefinition(
  providerURI = "${oidConfig.providerUrl}",
  redirectURI = "${oidConfig.redirectUrl}",
  redirectToOriginalResource = true,
  clientId = "${oidConfig.clientId}",
  clientSecret = "${oidConfig.clientSecret}",
  scope = {"openid", "email", "profile"}
)
@DeclareRoles("supervisor")
@ApplicationScoped
@ApplicationPath("/")
public class App extends Application {
}

