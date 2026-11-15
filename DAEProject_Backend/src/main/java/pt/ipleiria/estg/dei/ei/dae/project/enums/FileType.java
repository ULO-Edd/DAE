package pt.ipleiria.estg.dei.ei.dae.project.enums;

public enum FileType {
    CONFERENCE_PAPER("conferencePaper"),
    RESEARCH_POST("researchPost"),
    THESIS("thesis"),
    ARTICLE("article"),
    REPORT("report"),
    OTHER("other");

    private final String type;

    FileType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
