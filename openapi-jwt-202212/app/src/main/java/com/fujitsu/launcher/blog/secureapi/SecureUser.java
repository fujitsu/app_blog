package com.fujitsu.launcher.blog.secureapi;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.enterprise.context.RequestScoped;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;

@RequestScoped
@Path("/secureuser")
@SecurityScheme(securitySchemeName = "JWT-Auth",
		type = SecuritySchemeType.HTTP,
		scheme = "bearer",
		bearerFormat = "JWT",
		description = "JWTを使用したスキーマ。\n適切なJson Web TokenをAuthorizationヘッダに指定してください。")
public class SecureUser {
  @GET
  @Path("/{id}")
  @Produces("text/plain")
  @RolesAllowed("管理者")
  @Operation(summary = "JWTを使用したセキュアな呼出し")
  @Parameter(name = "id", description = "ユーザID")
  @APIResponse(responseCode = "200",
	       description = "指定したIDに該当するユーザの名前を表示します。",
	       content = @Content(mediaType = "text/plain"))
  @APIResponse(responseCode = "404", description = "指定したIDに該当するユーザが存在しません。")
  @APIResponse(responseCode = "401", description = "不適切なトークンによる呼出し。")
  @SecurityRequirement(name = "JWT-Auth")
  public Response user(@PathParam("id") int id) {
    String name = getName(id);
    if (name == null)
      return Response.status(Response.Status.NOT_FOUND).entity("該当ユーザは存在しません\n").build();
    else
      return Response.ok(name).build();
  }
  
  private String getName(int uid) {
    if (uid == 10)
      return "富士通太郎\n";
    else
      return null;
  }
}
