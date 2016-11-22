package encryptionservice;

import com.bettercloud.vault.Vault;
import com.bettercloud.vault.VaultConfig;
import com.bettercloud.vault.VaultException;
import com.bettercloud.vault.response.LogicalResponse;
import encryptionservice.entities.Content;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EncrytionService {

    public Content encryt(final Content content, final String token) throws VaultException {
        Map<String, String> options = new HashMap<>();
        options.put("plaintext", content.getData());
        LogicalResponse response = vaultFactory(token).logical().write("transit/encrypt/default", options);
        content.setData(response.getData().get("ciphertext"));
        return content;
    }

    public Content decrypt(final Content content, final String token) throws VaultException {
        Map<String, String> options = new HashMap<>();
        options.put("ciphertext", content.getData());
        LogicalResponse response = vaultFactory(token).logical().write("transit/decrypt/default", options);
        content.setData(response.getData().get("plaintext"));
        return content;
    }

    private Vault vaultFactory(final String token) throws VaultException {
        final VaultConfig config = new VaultConfig()
                .address("http://127.0.0.1:8200") // URL should be injected!
                .token(token)
                .build();
        return new Vault(config);
    }
}
