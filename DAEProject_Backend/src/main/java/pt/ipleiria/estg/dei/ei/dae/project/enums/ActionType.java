package pt.ipleiria.estg.dei.ei.dae.project.enums;

public enum ActionType {
    // User History Actions
    UPLOAD_PUBLICATION("uploadPublication"),
    COMMENT_PUBLICATION("commentPublication"),
    HIDE_PUBLICATION("hidePublication"),
    RATE_PUBLICATION("downvotedPublication"),
    EDIT_PUBLICATION("editPublication"),
    CHANGE_NAME("changeName"),
    CHANGE_EMAIL("changeEmail"),
    CHANGE_ROLE("changeRole"),
    CREATE_USER("createUser"),
    DELETE_USER("deleteUser"),
    EDIT_COMMENT("editComment"),
    HIDE_COMMENT("hideComment"),

    // Publication History Actions
    CREATE_PUBLICATION("createPublication"),
    TAG_PUBLICATION("tagPublication"),
    DELETE_PUBLICATION("deletePublication");
    

    private final String type;

    ActionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
