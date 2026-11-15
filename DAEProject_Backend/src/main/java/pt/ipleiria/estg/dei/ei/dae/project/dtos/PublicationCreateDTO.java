package pt.ipleiria.estg.dei.ei.dae.project.dtos;

import jakarta.json.bind.annotation.JsonbProperty; 
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.util.List;

@Getter @Setter @NoArgsConstructor
public class PublicationCreateDTO implements Serializable {
    
    private String title;
    
    private List<String> authors;

    @JsonbProperty("publication_date")
    private String publicationDate; 

    private String type;

    @JsonbProperty("abstract")
    private String abstractText; 

    @JsonbProperty("is_confidential")
    private boolean isConfidential;

    @JsonbProperty("tags")
    private List<Long> tags;
}
