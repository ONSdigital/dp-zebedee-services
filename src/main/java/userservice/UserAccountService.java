package userservice;

import com.bettercloud.vault.Vault;
import com.bettercloud.vault.VaultConfig;
import com.bettercloud.vault.VaultException;
import com.bettercloud.vault.response.AuthResponse;
import com.bettercloud.vault.response.LogicalResponse;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import userservice.entities.*;
import userservice.repositories.UserDetailsRepository;
import utils.CookieRestTemplate;

import java.util.*;
import java.util.stream.Collectors;

// Notes:
// Vault exception is leaking out of this class.

@Component
public class UserAccountService {

    private final String appRole;

    private final UserDetailsRepository userDetailsRepository;

    @Autowired
    public UserAccountService(final String appRole, final UserDetailsRepository userDetailsRepository) throws VaultException {
        this.appRole = appRole;
        this.userDetailsRepository = userDetailsRepository;
        this.userDetailsRepository.save(new UserDetails("admin@test.com", "admin"));
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

        userDetailsRepository.save(new UserDetails(user.getEmail(),user.getName()));
        vaultFactory(token)
                .logical()
                .write("auth/userpass/users/" + EmailUtils.convert(user.getEmail()), attributes);
    }

    public List<UserDetails> getUsers(final String token) throws VaultException {
        return vaultFactory(appRole)
                .logical()
                .list("auth/userpass/users/")
                .stream()
                .map(email -> new UserDetails(EmailUtils.revert(email),
                        userDetailsRepository.findByEmail(EmailUtils.revert(email)).get(0).getName())) // HACK
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
        final List<String> policies = Arrays.asList(response.getData().getOrDefault("policies", "").split(","));
        final Permission permission = new Permission();
        permission.setAdmin(policies.contains("admin")); // Buggy area, wrong permissions are set here.
        permission.setDataVisPublisher(policies.contains("visualisationpublisher"));
        permission.setEditor(policies.contains("publisher"));
        return permission;
    }

    public void deleteUser(final String email, final String token) throws VaultException {
        userDetailsRepository.delete(userDetailsRepository.findByEmail(email));
        vaultFactory(token)
                .logical()
                .delete("auth/userpass/users/" + EmailUtils.convert(email));
    }

    public Permission getPermissionFromToken(final String token) {
        final CookieRestTemplate template = new CookieRestTemplate(appRole);
        final JsonNode node = template.getForObject("http://127.0.0.1:8200/v1/auth/token/lookup/" + token, JsonNode.class);
        final List<String> policies = new ArrayList<>();
        final String email = EmailUtils.revert(node.get("data").get("meta").get("username").asText());
        node.get("data").get("policies").forEach(n -> policies.add(n.asText()));
        Permission permission = new Permission();
        permission.setAdmin(policies.contains("admin") || policies.contains("root"));
        permission.setEditor(policies.contains("publisher"));
        permission.setEmail(email);
        return permission;
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
