package pt.ipleiria.estg.dei.ei.dae.project.dtos;


import lombok.*;
import pt.ipleiria.estg.dei.ei.dae.project.entities.Tag;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO implements Serializable {

    private long id;
    @NonNull private String name;

    public static TagDTO from(Tag tag) {
        return new TagDTO(
                tag.getId(),
                tag.getName()
        );
    }

    public static List<TagDTO> from(List<Tag> tags) {
        return tags.stream().map(TagDTO::from).collect(Collectors.toList());
    }
}
