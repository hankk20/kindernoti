package kr.co.kindernoti.auth.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Getter @Setter
@ConfigurationProperties("application.jwt")
@Component
public class JwtProperties {

    private Resource privateKeyLocation;
    private Duration expire;
    private String issuer;

    public String readPrivateKey() {
        if (!privateKeyLocation.exists()) {
            throw new InvalidConfigurationPropertyValueException("application.jwt.privateKeyLocation", privateKeyLocation,
                    "key location does not exist");
        }
        return readKey(privateKeyLocation);
    }
    public String readKey(Resource resource) {
        try (InputStream inputStream = resource.getInputStream()) {
            return StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
