package userservice.entities;

public class UserDetails {

    private final String email;

    private final String username;

    public UserDetails(final String email) {
        this.email = email.replace("$", "@");
        this.username = "TODO";
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }
}
