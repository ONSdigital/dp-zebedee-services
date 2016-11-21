package userservice.entities;

public class User {

    private String email;

    private String name;

    private String password;

    private boolean isAdmin;

    private boolean isEditor;

    private boolean isDataVisPublisher;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isEditor() {
        return isEditor;
    }

    public void setIsEditor(boolean editor) {
        isEditor = editor;
    }

    public boolean isDataVisPublisher() {
        return isDataVisPublisher;
    }

    public void setIsDataVisPublisher(boolean dataVisPublisher) {
        isDataVisPublisher = dataVisPublisher;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
