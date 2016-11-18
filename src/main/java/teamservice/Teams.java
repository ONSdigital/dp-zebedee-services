package teamservice;


import org.springframework.web.client.RestTemplate;
import teamservice.entities.TeamList;

public class Teams {

    public TeamList getTeams(final String token) {
        RestTemplate template = new CookieRestTemplate(token);
        VaultPolicy policies = template.getForObject("http://127.0.0.1:8200/v1/sys/policy", VaultPolicy.class);
        return new TeamList(policies.getPolicies());
    }
}
