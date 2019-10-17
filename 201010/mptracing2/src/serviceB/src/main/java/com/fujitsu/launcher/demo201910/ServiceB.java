package com.fujitsu.launcher.demo201910;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.ClientBuilder;
import org.eclipse.microprofile.opentracing.Traced;

@RequestScoped
@Path("service")
public class ServiceB {

  @Inject
  ServiceClient client;
  
  @GET
  public String accept(@QueryParam("option") String option) {
    return client.call(option);
  }
}

@RequestScoped
class ServiceClient {
  @Traced
  public String call(String option) {
    String result = ClientBuilder
      .newClient()
      .target("http://localhost:6060/service?option=" + option) // call ServiceC
      .request()
      .get(String.class);
    return result;
  }
}

