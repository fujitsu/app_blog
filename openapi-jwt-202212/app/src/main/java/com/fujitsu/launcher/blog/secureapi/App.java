package com.fujitsu.launcher.blog.secureapi;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.auth.LoginConfig;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;

@ApplicationPath("/")
@LoginConfig(authMethod = "MP-JWT")
@OpenAPIDefinition(info = @Info(title = "MP JWTを使用したサンプル", version = "1.0",
description = "MicroProfile Interoperable JWT RBACと、MicroProfile OpenAPIを使用したサンプルアプリケーション",
contact = @Contact(name = "お問合わせ先", email = "contact@example.com", url = "http://example.com")) )
public class App extends Application {
}
