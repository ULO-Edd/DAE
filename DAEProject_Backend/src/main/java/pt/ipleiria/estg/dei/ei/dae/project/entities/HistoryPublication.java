package pt.ipleiria.estg.dei.ei.dae.project.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@NamedQueries({
        @NamedQuery(
                name = "getAllHistoryPublications",
                query = "SELECT hp FROM HistoryPublication hp ORDER BY hp.timestamp DESC"
        ),
        @NamedQuery(
                name = "getHistoryPublicationsByPublicationId",
                query = "SELECT hp FROM HistoryPublication hp WHERE hp.publication.id = :publicationId ORDER BY hp.timestamp DESC"
        )
})
@Getter 
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class HistoryPublication {
    @Id @GeneratedValue(strategy = GenerationType.AUTO) private long id;
    @ManyToOne @NonNull private Publication publication;
    @NonNull private Date timestamp = new Date();
    @NonNull private String actionType;
    @NonNull private long userId;
    private String description;

    public HistoryPublication(Publication publication, Date timestamp, String actionType, long userId, String description) {
        this.publication = publication;
        this.timestamp = timestamp;
        this.actionType = actionType;
        this.userId = userId;
        this.description = description;
    }
    
}
