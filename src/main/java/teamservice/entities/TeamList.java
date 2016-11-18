package teamservice.entities;


import java.util.List;

public class TeamList {
    private List<String> teams;

    public TeamList(List<String> teams) {
        this.teams = teams;
    }

    public List<String> getTeams() {
        return teams;
    }

    public void setTeams(List<String> teams) {
        this.teams = teams;
    }
}
