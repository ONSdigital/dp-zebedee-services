package userservice.entities;

public class Response {
    private boolean completed;
    private String error;

    public Response(final boolean completed, final String error) {
        this.completed = completed;
        this.error = error;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
