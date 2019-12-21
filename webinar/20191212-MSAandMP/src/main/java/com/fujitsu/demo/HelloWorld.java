package com.fujitsu.demo;

import java.util.Random;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;

@RequestScoped
@Path("hello")
public class HelloWorld {

  @Timeout(1000)
  @Fallback(fallbackMethod = "fallback")
  @GET
  public String hello() {
    if (new Random().nextInt() % 3 == 0) {
      try {
        Thread.sleep(5000);
      } catch (Exception e) {}
      return "Hello Sleep World";
    }
    return "Hello Wakeup World";
  }

  private String fallback() {
    return "Hello Fallback World";
  }
}

