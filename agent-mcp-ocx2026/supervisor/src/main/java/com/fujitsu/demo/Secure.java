/*
 * Copyright (c) 2026 Fujitsu Limited. All rights reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.fujitsu.demo;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.security.enterprise.identitystore.openid.OpenIdContext;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;

@RequestScoped
@Path("secure")
public class Secure {

  @Inject
  private OpenIdContext context;

  @POST
  @Path("/ask")
  @RolesAllowed("supervisor")
  @Produces(MediaType.TEXT_HTML)
  public Response ask(@FormParam("request") String request) throws Exception {
    IO.println("Your request is : " + request);
 
    var res = callAgent(request);
    var html = """
         <!DOCTYPE html><html lang="en">
         <head><meta charset="utf-8" /></head>
         <body><h2>
         %s
         </h2></body>
       """.formatted(res);
    return Response
      .status(Response.Status.OK)
      .entity(html)
      .build();
  }

  @Inject
  SupervisorAgent agent;

  private String callAgent(String question)  {
      var res = agent.invoke(question);
      return res;
  }


  @GET
  @Path("/top")
  @RolesAllowed("supervisor")
  @Produces(MediaType.TEXT_HTML)
  public Response top() throws Exception {
    var yourName = context.getClaims().getName().get();

    var html = """
        <!DOCTYPE html><html lang="en"><head><meta charset="utf-8" /></head>
        <html><body><h2>
          Hello %s!<br>
          What can I help you ?
          <form action="/secure/ask" method="POST">
          <textarea name="request" cols="100" rows="5"></textarea><br>
         <input type="submit" value="ask">
        </form>
        </h2></body></html>
      """.formatted(yourName);

    return Response
      .status(Response.Status.OK)
      .entity(html)
      .build();
  }
  

}
