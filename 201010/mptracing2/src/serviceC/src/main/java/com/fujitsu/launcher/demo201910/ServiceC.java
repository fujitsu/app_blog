package com.fujitsu.launcher.demo201910;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import io.opentracing.Tracer;
import io.opentracing.Span;
import javax.inject.Inject;

@RequestScoped
@Path("service")
public class ServiceC {

  @Inject
  Tracer tracer;
    
  @GET
  public String accept(@QueryParam("option") String option) {
    if (option.equals("sleep")) {
      Span span = tracer.buildSpan("PORTION").start();
      try {
	Thread.sleep(500);   // intentionally delay response
      } catch (Exception e) {}
      span.log("only portion");
      span.finish();
    }
    return "Service accepted : " + option + "\n";
  }

}
