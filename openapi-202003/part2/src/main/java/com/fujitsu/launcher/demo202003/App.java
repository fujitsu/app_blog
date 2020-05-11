package com.fujitsu.launcher.demo202003;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.ExternalDocumentation;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.Contact;

@ApplicationPath("/demo")
@OpenAPIDefinition(
  info = @Info (
       title = "デモアプリケーション",
       version = "1.2.3",
       description = "MP OpenAPIのアプリケーション",
       contact = @Contact(url = "http://example.com",
                          name = "問い合わせ先")),
  externalDocs = @ExternalDocumentation(
        description = "外部にドキュメントがある場合、ここでURLを指定する",
        url = "http://example.com")
)
public class App extends Application {
}
