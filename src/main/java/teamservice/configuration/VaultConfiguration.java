package teamservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VaultConfiguration {

    @Bean
    String vaultToken() {
        final String vaultToken = System.getenv("VAULT_TOKEN");
        if (vaultToken == null) throw new RuntimeException("Vault token not set, use export VAULT_TOKEN=<token>");
        return vaultToken;
    }
}
