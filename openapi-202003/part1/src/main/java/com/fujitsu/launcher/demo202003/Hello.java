package com.fujitsu.launcher.demo202003;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("hello")
public class Hello {

  @GET
  public String hello() {
    return "Hello World !";
  }
}
