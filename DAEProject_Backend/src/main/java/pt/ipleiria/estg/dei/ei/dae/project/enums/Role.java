package pt.ipleiria.estg.dei.ei.dae.project.enums;

public enum Role {
    COLLABORATOR("collaborator"),
    MODERATOR("moderator"),
    ADMINISTRATOR("administrator");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
