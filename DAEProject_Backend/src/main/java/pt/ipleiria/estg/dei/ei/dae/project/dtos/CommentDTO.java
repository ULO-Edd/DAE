package pt.ipleiria.estg.dei.ei.dae.project.dtos;

import lombok.*;
import pt.ipleiria.estg.dei.ei.dae.project.entities.Comment;
import pt.ipleiria.estg.dei.ei.dae.project.entities.Publication;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO implements Serializable {

    @NonNull private long id;
    @NonNull private PublicationDetailsDTO publication;
    @NonNull private UserDTO author;
    @NonNull private String content;
    @NonNull private Date createdAt;
    @NonNull private boolean isHidden;

    public static CommentDTO from(Comment comment) {
        Publication p = comment.getPublication();

        PublicationDetailsDTO pubDTO = new PublicationDetailsDTO();
        pubDTO.setId(p.getId());
        pubDTO.setTitle(p.getTitle());
        pubDTO.setAuthors(p.getAuthors());
        pubDTO.setPublicationDate(p.getPublicationDate());
        pubDTO.setType(p.getType());
        pubDTO.setRating((int) p.getRating());

        // Tags are EAGER, so safe to map
        if (p.getTags() != null) {
            pubDTO.setTags(p.getTags().stream().map(TagDTO::from).collect(Collectors.toList()));
        } else {
             pubDTO.setTags(new java.util.ArrayList<>());
        }

        // Avoid touching p.getComments() to prevent LazyInitializationException
        pubDTO.setCommentCount(0);
        pubDTO.setComments(new java.util.ArrayList<>());

        pubDTO.setSubmitter(UserDTO.from(p.getSubmitter()));
        pubDTO.setAttachedFile(p.getAttachedFile());
        pubDTO.setAbstractText(p.getAbstractText());
        pubDTO.setHidden(p.isHidden());

        return new CommentDTO(
                comment.getId(),
                pubDTO,
                UserDTO.from(comment.getAuthor()),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.isHidden()
        );
    }

    public static List<CommentDTO> from(List<Comment> comments) {
        return comments.stream().map(CommentDTO::from).collect(Collectors.toList());
    }
}
