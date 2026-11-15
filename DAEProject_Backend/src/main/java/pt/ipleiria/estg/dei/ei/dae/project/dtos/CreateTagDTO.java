package pt.ipleiria.estg.dei.ei.dae.project.dtos;


import lombok.*;
import pt.ipleiria.estg.dei.ei.dae.project.entities.Tag;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class CreateTagDTO implements Serializable {

    @NonNull private String name;

    public static CreateTagDTO from(Tag tag) {
        return new CreateTagDTO(
                tag.getName()
        );
    }

    public static List<CreateTagDTO> from(List<Tag> tags) {
        return tags.stream().map(CreateTagDTO::from).collect(Collectors.toList());
    }

}
