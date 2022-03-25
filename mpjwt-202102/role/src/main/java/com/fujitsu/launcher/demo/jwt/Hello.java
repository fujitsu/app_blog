package com.fujitsu.launcher.demo.jwt;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.enterprise.context.RequestScoped;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.ClaimValue;

@Path("hello")
@RequestScoped
public class Hello {

  @Inject
  @Claim("upn")
  private ClaimValue<String> upn;

  @GET
  @RolesAllowed("キツネさんチーム")
  public String hello() {
    return "Hello  " + upn.getValue() + " !\n";
  }
}
