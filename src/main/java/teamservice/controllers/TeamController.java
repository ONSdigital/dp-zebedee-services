package teamservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teamservice.Teams;
import teamservice.entities.Team;
import teamservice.entities.TeamList;

import java.util.Collections;

@RestController
public class TeamController {

    @Autowired
    private Teams teams;

    @Autowired
    private String vaultToken;

    @RequestMapping(value = "/team")
    public TeamList getTeams() {
        return teams.getTeams(vaultToken);
    }

    @RequestMapping(value = "/team/{teamName}")
    public Team getTeams(final @PathVariable String teamName) {
        return teams.getTeam(teamName,vaultToken);
    }
}
