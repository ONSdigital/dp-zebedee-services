package teamservice.entities;


import java.util.List;

public class TeamList {
    private List<Team> teams;

    public TeamList(List<Team> teams) {
        this.teams = teams;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }
}
