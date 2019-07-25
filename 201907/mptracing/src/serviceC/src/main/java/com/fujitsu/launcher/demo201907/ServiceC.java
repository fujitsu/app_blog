package com.fujitsu.launcher.demo201907;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@RequestScoped
@Path("service")
public class ServiceC {

  @GET
  public String accept(@QueryParam("option") String option) {
    if (option.equals("sleep"))
      try {
	Thread.sleep(500);   // intentionally delay response
      } catch (Exception e) {}
    return "Service accepted : " + option + "\n";
  }

}
