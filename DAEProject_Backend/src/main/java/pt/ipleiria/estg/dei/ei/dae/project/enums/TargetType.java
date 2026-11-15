package pt.ipleiria.estg.dei.ei.dae.project.enums;

public enum TargetType {
    PUBLICATION("publication"),
    COMMENT("comment"),
    USER("user"),
    ROLE("role"),
    VOTE("vote");

    private final String type;

    TargetType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
