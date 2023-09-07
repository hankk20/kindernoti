package kr.co.kindernoti.auth.security.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

public class JwtRsaKeyGenerator {
    public static void main(String[] args) throws JOSEException, ParseException {
        RSAKey generate = new RSAKeyGenerator(2048)
                .keyUse(KeyUse.SIGNATURE)
                .keyID(UUID.randomUUID().toString())
                .issueTime(new Date())
                .generate();
        System.out.println(generate);
        JWSObject test = new JWSObject(new JWSHeader(JWSAlgorithm.RS256), new Payload("test"));
        test.sign(new RSASSASigner(generate));
        System.out.println(test.serialize());
        String serialize = test.serialize();
        JWSObject parse = JWSObject.parse(serialize);
        boolean verify = parse.verify(new RSASSAVerifier(generate.toRSAPublicKey()));
        System.out.println(verify);
        System.out.println(parse.getSignature());
        Payload payload = parse.getPayload();

    }
}
