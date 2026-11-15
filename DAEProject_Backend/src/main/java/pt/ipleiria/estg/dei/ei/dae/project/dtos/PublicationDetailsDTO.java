package pt.ipleiria.estg.dei.ei.dae.project.dtos;

import lombok.*;
import pt.ipleiria.estg.dei.ei.dae.project.entities.Publication;
import pt.ipleiria.estg.dei.ei.dae.project.enums.FileType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PublicationDetailsDTO extends PublicationSummaryDTO implements Serializable {

    private String attachedFile;
    private List<CommentDTO> comments;

    public PublicationDetailsDTO(
            Long id,
            UserDTO submitter,
            String title,
            List<String> authors,
            Date publicationDate,
            String attachedFile,
            FileType fileType,
            String abstractText,
            boolean isHidden,
            int rating,
            List<TagDTO> tags,
            List<CommentDTO> comments
    ) {
        super(id, submitter, title, authors, publicationDate, fileType, rating, comments != null ? comments.size() : 0, tags, isHidden, abstractText);
        this.attachedFile = attachedFile;
        this.comments = comments;
    }

    public static PublicationDetailsDTO from(Publication publication) {
        List<TagDTO> tagsDTO = new ArrayList<>();
        if (publication.getTags() != null) {
             tagsDTO = publication.getTags().stream().map(TagDTO::from).collect(Collectors.toList());
        }

        List<CommentDTO> commentsDTO = new ArrayList<>();
        if (publication.getComments() != null) {
             commentsDTO = publication.getComments().stream().map(CommentDTO::from).collect(Collectors.toList());
        }

        return new PublicationDetailsDTO(
                publication.getId(),
                UserDTO.from(publication.getSubmitter()), 
                publication.getTitle(),
                publication.getAuthors(),
                publication.getPublicationDate(),
                publication.getAttachedFile(),
                publication.getType(),
                publication.getAbstractText(),
                publication.isHidden(),
                (int) publication.getRating(), 
                tagsDTO,
                commentsDTO
        );
    }
}
