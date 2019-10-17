package com.fujitsu.launcher.demo201910;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.ClientBuilder;
import io.opentracing.Tracer;
import io.opentracing.Span;
import javax.inject.Inject;

@RequestScoped
@Path("service")
public class ServiceA {

  @Inject
  Tracer tracer;
  
  @GET
  public String accept(@QueryParam("option") String option) {
    String result = ClientBuilder
      .newClient()
      .target("http://localhost:7070/service?option=" + option) // call ServiceB
      .request()
      .get(String.class);
    tracer.activeSpan().log("serviceA returns " + result);
    return result;
  }
}
