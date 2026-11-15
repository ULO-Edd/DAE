package pt.ipleiria.estg.dei.ei.dae.project.dtos;

import lombok.*;
import pt.ipleiria.estg.dei.ei.dae.project.entities.Comment;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class CommentPayloadDTO implements Serializable {

    @NonNull private String content;

    public static CommentPayloadDTO from(Comment comment) {

        return new CommentPayloadDTO(
                comment.getContent()
        );
    }

    public static List<CommentPayloadDTO> from(List<Comment> comments) {
        return comments.stream().map(CommentPayloadDTO::from).collect(Collectors.toList());
    }
}
