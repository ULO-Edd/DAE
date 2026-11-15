package pt.ipleiria.estg.dei.ei.dae.project.dtos;

import lombok.*;
import pt.ipleiria.estg.dei.ei.dae.project.entities.Publication;
import pt.ipleiria.estg.dei.ei.dae.project.enums.FileType;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PublicationSummaryDTO implements Serializable {
    @NonNull private  Long id;
    @NonNull private UserDTO submitter;
    @NonNull private String title;
    @NonNull private List<String> authors;
    @NonNull private Date publicationDate;
    @NonNull private FileType type;
    @NonNull private long rating;
    @NonNull private int commentCount;
    @NonNull private List<TagDTO> tags;
    private boolean isHidden;
    private String abstractText;

    public static PublicationSummaryDTO from(Publication publication) {
        return new PublicationSummaryDTO(
                publication.getId(),
                UserDTO.from(publication.getSubmitter()),
                publication.getTitle(),
                publication.getAuthors(),
                publication.getPublicationDate(),
                publication.getType(),
                publication.getRating(),
                publication.getComments() != null ? publication.getComments().size() : 0,
                publication.getTags().stream().map(TagDTO::from).collect(Collectors.toList()),
                publication.isHidden(),
                publication.getAbstractText()
        );
    }

    public static List<PublicationSummaryDTO> from(List<Publication> publications) {
        return publications.stream().map(PublicationSummaryDTO::from).collect(Collectors.toList());
    }
}
