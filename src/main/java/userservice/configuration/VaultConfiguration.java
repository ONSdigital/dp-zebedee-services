package userservice.configuration;

import com.bettercloud.vault.VaultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import userservice.UserAccountService;

@Configuration
public class VaultConfiguration {

    @Bean
    String vaultId() {
        final String vaultId = System.getenv("VAULT_TOKEN");
        if (vaultId == null) throw new RuntimeException("Vault token not set, use export VAULT_TOKEN=<token>");
        return vaultId;
    }

    @Bean
    UserAccountService vaultService(@Autowired String vaultId) throws VaultException {
        return new UserAccountService(vaultId);
    }
}
