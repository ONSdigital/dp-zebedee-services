package teamservice.entities;

import java.util.HashSet;
import java.util.Set;

public class Team {
    private int id;
    private String name;
    private Set<String> members;

    public Team(String name) {
        this.name = name;
        this.members = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getMembers() {
        return members;
    }

    public void setMembers(Set<String> members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return name + " (" + id + ") ";
    }
}
