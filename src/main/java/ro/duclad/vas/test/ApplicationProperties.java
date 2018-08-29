package ro.duclad.vas.test;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "application")
@Component
@Data
public class ApplicationProperties {
    private String jsonfilesLocation;
    private String words;
}
