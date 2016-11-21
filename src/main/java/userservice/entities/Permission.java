package userservice.entities;

public class Permission {

    private String email;

    private boolean admin;

    private boolean editor;

    private boolean dataVisPublisher;

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isEditor() {
        return editor;
    }

    public void setEditor(boolean editor) {
        this.editor = editor;
    }

    public boolean isDataVisPublisher() {
        return dataVisPublisher;
    }

    public void setDataVisPublisher(boolean dataVisPublisher) {
        this.dataVisPublisher = dataVisPublisher;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
