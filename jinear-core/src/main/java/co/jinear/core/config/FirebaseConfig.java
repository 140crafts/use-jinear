package co.jinear.core.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Configuration
public class FirebaseConfig {

    @Value("${notification.firebase.auth}")
    private String firebaseAuth;

    @Value("${notification.firebase.enabled}")
    private Boolean enabled;

    @PostConstruct
    public void initializeFirebase() throws IOException {
        if (Boolean.TRUE.equals(enabled)) {
            byte[] decoded = Base64.getDecoder().decode(firebaseAuth);
            InputStream tokenStream = new ByteArrayInputStream(decoded);
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(tokenStream))
                    .build();
            FirebaseApp.initializeApp(options);
        }
    }
}
