# Agent server and MCP server

This code is used at OCX 2026.  
The demonstration for communication between Agent server and MCP server using Jakarta EE and MicroProfile technologies.


## Agent Server

### Preparation

* Set the following envritonment variables:
  * OPENID_REDIRECT_URL
  * OPENID_PROVIDER_URL
  * OPENID_CLIENT_ID
  * OPENID_CLIENT_SECRET

  Note that 'OPENID_REDIRECT_URL' is supposed to be 'http://localhost:8080/secure/Callback' in this program.

* Customize the model to be used.  
  This program use Gemini as the model, but you can change it to any model.
  You need to change 'BanAgentFactory.java' and 'SupervisorAgentFactory.java'.
  

### How to build

```
 % mvn clean package
```

### How to run

Example of using GlassFish :

```
 % java glassfish-embedded-all-8.0.0.jar target/supervisor.war
```


## MCP server

### Preparation

* Customize 'microprofile-config.properties' at 'bank/src/main/resources/META-INF'  
  For the details see 'microprofile-config.properties'.

* To use JWT Ahthenication with GlassFish, you need to implement 
'jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanismHandler'.  
For the detail see 'JWTAuthHandler.java'

### Build

```
 % mvn clean package
```

### Run

Example of using GlassFish :

```
 % java glassfish-embedded-all-8.0.0.jar -p 9090 target/bank.war
```



