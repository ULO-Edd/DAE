package pt.ipleiria.estg.dei.ei.dae.project.dtos;

import lombok.*;
import pt.ipleiria.estg.dei.ei.dae.project.entities.Publication;
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
public class HistoryUserDTO implements Serializable {

    @NonNull private long id;
    @NonNull private Date timestamp;
    @NonNull private ActionType actionType;
    @NonNull private TargetType targetType;
    @NonNull private long targetId;
    private String description;

    public static HistoryUserDTO from(HistoryUser historyUser) {
        
        return new HistoryUserDTO(
                historyUser.getId(),
                historyUser.getTimestamp(),
                ActionType.valueOf(historyUser.getActionType()),
                TargetType.valueOf(historyUser.getTargetType()),
                historyUser.getTargetId(),
                historyUser.getDescription()
        );
    }

    public static List<HistoryUserDTO> fromList(List<HistoryUser> historyUsers) {
        return historyUsers.stream().map(HistoryUserDTO::from).collect(Collectors.toList());
    }
}

