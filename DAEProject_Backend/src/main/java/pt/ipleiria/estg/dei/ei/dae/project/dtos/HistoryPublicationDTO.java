package pt.ipleiria.estg.dei.ei.dae.project.dtos;

import lombok.*;
import pt.ipleiria.estg.dei.ei.dae.project.entities.Publication;
import pt.ipleiria.estg.dei.ei.dae.project.entities.HistoryPublication;
import pt.ipleiria.estg.dei.ei.dae.project.entities.User;

import pt.ipleiria.estg.dei.ei.dae.project.enums.ActionType;
import pt.ipleiria.estg.dei.ei.dae.project.enums.TargetType;

import pt.ipleiria.estg.dei.ei.dae.project.entities.HistoryUser;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoryPublicationDTO implements Serializable {
    @NonNull private long id;
    @NonNull private Date timestamp;
    @NonNull private ActionType actionType; 
    @NonNull private long publicationId;
    @NonNull private long userId;
    private String description;

    public static HistoryPublicationDTO from(HistoryPublication historyPublication) {
        
        return new HistoryPublicationDTO(
                historyPublication.getId(),
                historyPublication.getTimestamp(),
                ActionType.valueOf(historyPublication.getActionType()),
                historyPublication.getPublication().getId(),
                historyPublication.getUserId(),
                historyPublication.getDescription()
        );
    }

    public static List<HistoryPublicationDTO> fromList(List<HistoryPublication> historyPublications) {
        return historyPublications.stream().map(HistoryPublicationDTO::from).collect(Collectors.toList());
    }
}
