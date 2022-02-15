/**
  Do not use this program in your production code.
  This is just a demo for creating JWT.
 */
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.KeyPairGenerator;
import java.security.KeyPair;
import java.security.Signature;
import java.security.spec.KeySpec;
import java.security.spec.ECPoint;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.util.Base64;
import java.io.BufferedReader;
import java.io.FileReader;

/**
  Usage: java JWTGenerator json-header-file json-payload-file
 */
public class JWTGenerator {
  PrivateKey prvKey;
  PublicKey pubKey;

  public static void main(String ... arg) throws Exception {
    JWTGenerator gen = new JWTGenerator();
    gen.generateKeyECDSA();
    gen.generateJWT(arg[0], arg[1]);
  }

  void generateJWT(String headerFile, String payloadFile) throws Exception {
    String header = base64(headerFile);
    String payload = base64(payloadFile);

    Signature si = Signature.getInstance("SHA256withECDSAinP1363Format");
    si.initSign(prvKey);
    si.update(header.getBytes("US-ASCII"));
    si.update(".".getBytes());
    si.update(payload.getBytes("US-ASCII"));
    String signature = Base64.getUrlEncoder().encodeToString(si.sign());
    System.out.println("\nJWT:");
    System.out.println(header + "." + payload + "." + signature);
  }

  String base64(String file) throws Exception {
    BufferedReader br = new BufferedReader(new FileReader(file));
    StringBuilder sb = new StringBuilder();
    while (true) {
      String str = br.readLine();
      if (str == null)
	break;
      sb = sb.append(str);
    }
    return Base64.getUrlEncoder().encodeToString(sb.toString().getBytes("UTF-8"));
  }

  void generateKeyECDSA() throws Exception {
    KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
    kpg.initialize(new ECGenParameterSpec("secp256r1"));
    KeyPair kp = kpg.genKeyPair();
    prvKey = kp.getPrivate();
    pubKey = kp.getPublic();
    ECPublicKey eckey = (ECPublicKey)pubKey;
    ECPoint point = eckey.getW();
    
    // make JWK format
    StringBuilder sb = new StringBuilder().
      append("{").
      append("\"kty\":\"EC\",").
      append("\"crv\":\"P-256\",").
      append("\"x\":\"").
      append(Base64.getUrlEncoder().encodeToString(point.getAffineX().toByteArray())).
      append("\",").
      append("\"y\":\"").
      append(Base64.getUrlEncoder().encodeToString(point.getAffineY().toByteArray())).
      append("\"").
      append("}");

    System.out.println("Public Key JWK:");
    System.out.println(Base64.getUrlEncoder().encodeToString(sb.toString().getBytes("US-ASCII")));

    System.out.println("Public Key PEM:");
    System.out.println(Base64.getMimeEncoder().encodeToString(pubKey.getEncoded()));
  }
}
 
