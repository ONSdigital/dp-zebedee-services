package teamservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teamservice.Teams;
import teamservice.entities.TeamList;

import java.util.Collections;

@RestController
public class TeamController {

    @Autowired
    private String vaultToken;

    @RequestMapping(value = "/team")
    public TeamList getTeams() {
        return new Teams().getTeams(vaultToken);
    }
}
