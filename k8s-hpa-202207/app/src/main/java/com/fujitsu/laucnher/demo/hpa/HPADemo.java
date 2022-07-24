package com.fujitsu.launcher.demo.hpa;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.annotation.Metric;

@Path("hello")
@RequestScoped
public class HPADemo {
  @Inject
  @Metric(absolute=true, name="demo")
  Counter counter;

  @GET
  @Path("dec")
  public String decrement() {
    counter.inc(-1);
    return "Hello " + counter.getCount() + "\n";
  }

  @GET
  @Path("inc")
  public String increment() {
    counter.inc();
    return "Hello " + counter.getCount() + "\n";
  }

}

