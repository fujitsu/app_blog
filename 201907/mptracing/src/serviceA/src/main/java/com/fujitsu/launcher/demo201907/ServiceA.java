package com.fujitsu.launcher.demo201907;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.ClientBuilder;

@RequestScoped
@Path("service")
public class ServiceA {

  @GET
  public String accept(@QueryParam("option") String option) {
    String result = ClientBuilder
      .newClient()
      .target("http://localhost:7070/service?option=" + option) // call ServiceB
      .request()
      .get(String.class);
    return result;
  }
}
