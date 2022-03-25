package com.fujitsu.launcher.demo.jwt;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.enterprise.context.RequestScoped;

@Path("hello")
@RequestScoped
public class Hello {
  @GET
  public String hello() {
    return "Hello !\n";
  }
}
