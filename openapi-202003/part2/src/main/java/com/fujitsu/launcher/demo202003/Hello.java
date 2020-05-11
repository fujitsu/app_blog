package com.fujitsu.launcher.demo202003;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;

@Path("hello")
public class Hello {

  @GET
  public String hello() {
    return "Hello World !";
  }

  @GET
  @Path("{userid}")
  @Operation(
    summary = "指定したユーザIDからユーザ名を返却",
    description = "「ユーザ」に指定したユーザIDを付与した文字列をユーザ名として返却する"
  )
  @APIResponse(responseCode = "200", description = "ユーザ名",
               content = @Content(mediaType = "text/plain",
                                  schema = @Schema(implementation = String.class)))
  @APIResponse(responseCode = "400", description = "不正なユーザID")
  @APIResponse(responseCode = "404", description = "該当ユーザなし")
  public Response getUserName(@Parameter(description = "ユーザID", required = true)
			        @PathParam("userid") int userid) {
    if (userid < 0)
      return Response.status(400).build();
    else if (userid > 100)
      return Response.status(404).build();
    return Response.ok("ユーザ" + userid).build();
  }
}
