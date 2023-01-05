# MicroProfile OpenAPIで、セキュリティスキーマの使用

MicroProfile Interoperable JWT RBACを使用したセキュアAPIについて、
MicroProfile OpenAPIでドキュメント化する方法例。

## 準備
Launcherを```https://github.com/fujitsu/launcher/releases```からダウンロードする。
以下の例では、launcher-4.0.jarを使用している。

## JWK/JWTの生成

```
$ cd jwt-gen
$ javac JWTGenerator.java
$ java -Dfile.encoding=UTF-8 JWTGenerator header.txt payload.txt
```

JWKとJWTが生成されるので、メモしておく。

## アプリケーションのビルド

```
$ cd app
$ mvn clean package
$ cd ..
$ java -Dmp.jwt.verify.publickey=*** -Dmp.jwt.verify.publickey.algorithm=ES256 -jar launcher-4.0.jar --deploy app/target/secureapi-1.0.war
```
(***は、```JWK/JWTの生成```で作成したJWKを指定する。)


## アプリケーションの実行

```
$ curl localhost:8080/user/10 -v -H "Authorization: Bearer ***"
```
(***は、```JWK/JWTの生成```で作成したJWTを指定する。)


## Swagger UIの使用

ブラウザから、以下のURLにアクセスする。

```
http://localhost:8080/openapi-ui/
```
