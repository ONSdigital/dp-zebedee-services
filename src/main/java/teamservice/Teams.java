package teamservice;


import com.bettercloud.vault.Vault;
import com.bettercloud.vault.VaultConfig;
import com.bettercloud.vault.VaultException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import teamservice.entities.Team;
import teamservice.entities.TeamList;
import userservice.entities.EmailUtils;
import utils.CookieRestTemplate;
import utils.VaultPolicy;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Teams {

    private Map<String, Team> teamsCache;

    @Autowired
    public Teams(final String token) throws VaultException {
        teamsCache = new HashMap<>();
        RestTemplate template = new CookieRestTemplate(token);
        VaultPolicy policies = template.getForObject("http://127.0.0.1:8200/v1/sys/policy", VaultPolicy.class);
        policies.getPolicies()
                .stream()
                .filter((policy) -> policy.contains("team"))
                .map((team) -> team.replace("team.", ""))
                .map((choppedName) -> new Team(choppedName))
                .forEach((team) -> teamsCache.put(team.getName(), team));

        vaultFactory(token)
                .logical()
                .list("auth/userpass/users/")
                .stream()
                .forEach((user) -> {
                    JsonNode json = new CookieRestTemplate(token).getForObject("http://127.0.0.1:8200/v1/auth/userpass/users/" + user, JsonNode.class);
                    Arrays.asList(json.get("data").get("policies").asText().split(","))
                                .forEach((policy) -> addUserToTeam(user, policy));
                });
    }

    public TeamList getTeams(final String token) {
        List<Team> teams = new ArrayList<>(teamsCache.values());
        return new TeamList(teams);
    }

    private void addUserToTeam(String user, String teamName) {
        Team team= teamsCache.get(teamName.replace("team.", ""));
        if(team != null) {
            team.getMembers().add(EmailUtils.revert(user));
        }
    }

    private Vault vaultFactory(final String token) throws VaultException {
        final VaultConfig config = new VaultConfig()
                .address("http://127.0.0.1:8200") // URL should be injected!
                .token(token)
                .build();
        return new Vault(config);
    }

    public Team getTeam(String teamName, String vaultToken) {
        return teamsCache.get(teamName);
    }
}
