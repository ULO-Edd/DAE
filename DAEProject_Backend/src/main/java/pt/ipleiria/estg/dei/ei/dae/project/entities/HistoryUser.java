package pt.ipleiria.estg.dei.ei.dae.project.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@NamedQueries({
        @NamedQuery(
                name = "getAllHistoryUsers",
                query = "SELECT hu FROM HistoryUser hu ORDER BY hu.timestamp DESC"
        ),
        @NamedQuery(
                name = "getHistoryUsersByUserId",
                query = "SELECT hu FROM HistoryUser hu WHERE hu.user.id = :userId ORDER BY hu.timestamp DESC"
        )
})
@Getter 
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class HistoryUser {
    @Id @GeneratedValue(strategy = GenerationType.AUTO) private long id;
    @ManyToOne @NonNull private User user;
    @NonNull private Date timestamp = new Date();
    @NonNull private String actionType;
    @NonNull private String targetType;
    @NonNull private long targetId;
    private String description;

    public HistoryUser(User user, Date timestamp, String actionType, String targetType, long targetId, String description) {
        this.user = user;
        this.timestamp = timestamp;
        this.actionType = actionType;
        this.targetType = targetType;
        this.targetId = targetId;
        this.description = description;
    }
}
