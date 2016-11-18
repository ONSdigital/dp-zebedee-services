package userservice;

import com.bettercloud.vault.Vault;
import com.bettercloud.vault.VaultConfig;
import com.bettercloud.vault.VaultException;
import com.bettercloud.vault.response.AuthResponse;
import com.bettercloud.vault.response.LogicalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import userservice.entities.*;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserAccountService {

    private final String appRole;

    @Autowired
    public UserAccountService(final String appRole) throws VaultException {
        this.appRole = appRole;
    }

    public Optional<Token> authenicate(final User user) throws VaultException {
        AuthResponse response = vaultFactory(appRole)
                .auth()
                .loginByUserPass(EmailUtils.convert(user.getEmail()), user.getPassword());
        if (response.getAuthPolicies().contains("reset")) {
            return Optional.empty();
        }
        return Optional.of(new Token(response.getAuthClientToken()));
    }

    public void createUser(final User user, final String token) throws VaultException {
        final Map<String, String> attributes = new HashMap<>();
        attributes.put("password", user.getPassword());
        attributes.put("policies", policiesToString(user, true));
        vaultFactory(token)
                .logical()
                .write("auth/userpass/users/" + EmailUtils.convert(user.getEmail()), attributes);
    }

    public List<UserDetails> getUsers(final String token) throws VaultException {
        return vaultFactory(token)
                .logical()
                .list("auth/userpass/users/")
                .stream()
                .map(email -> new UserDetails(EmailUtils.revert(email)))
                .collect(Collectors.toList());
    }

    public void resetPassword(PasswordReset password) throws VaultException {
        final Map<String, String> attributes = new HashMap<>();
        AuthResponse response = vaultFactory(appRole)
                .auth()
                .loginByUserPass(EmailUtils.convert(password.getEmail()), password.getOldPassword());
        attributes.put("password", password.getNewPassword());

        response.getAuthPolicies().remove("reset");
        String policies = "";
        for (String policy : response.getAuthPolicies()) {
            policies += policy + ",";
        }
        attributes.put("policies", policies);
        vaultFactory(appRole)
                .logical().
                write("auth/userpass/users/" + EmailUtils.convert(password.getEmail()), attributes);
    }

    public Permission getUsersPolicies(final String email) throws VaultException {
        LogicalResponse response = vaultFactory(appRole)
                .logical()
                .read("auth/userpass/users/" + EmailUtils.convert(email));
        final String policies = response.getData().getOrDefault("policies", "");
        final Permission permission = new Permission();
        permission.setAdmin(policies.contains("admin"));
        permission.setEditor(policies.contains("publisher"));
        return permission;
    }

    public void deleteUser(final String email, final String token) throws VaultException {
        vaultFactory(token)
                .logical()
                .delete("auth/userpass/users/" + EmailUtils.convert(email));
    }

    private String policiesToString(final User user, final boolean reset) {
        String policies = "";
        policies += user.isAdmin() ? "admin," : "";
        policies += user.isEditor() ? " publisher," : "";
        policies += user.isDataVisPublisher() ? "visualisationPublisher," : "";
        policies += reset ? "reset," : "";
        return policies;
    }

    private Vault vaultFactory(final String token) throws VaultException {
        final VaultConfig config = new VaultConfig()
                .address("http://127.0.0.1:8200") // URL should be injected!
                .token(token)
                .build();
        return new Vault(config);
    }
}
