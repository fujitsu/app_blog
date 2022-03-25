package com.fujitsu.launcher.demo.jwt;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import org.eclipse.microprofile.auth.LoginConfig;

@ApplicationPath("/")
@LoginConfig(authMethod = "MP-JWT")
public class App extends Application {
}
