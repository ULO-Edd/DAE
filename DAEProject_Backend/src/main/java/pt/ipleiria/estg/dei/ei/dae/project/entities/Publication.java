package pt.ipleiria.estg.dei.ei.dae.project.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import pt.ipleiria.estg.dei.ei.dae.project.enums.FileType;
import pt.ipleiria.estg.dei.ei.dae.project.enums.VoteType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "publications")
@NoArgsConstructor
@RequiredArgsConstructor
@NamedQueries({
    @NamedQuery(
        name = "Publication.countAll", 
        query = "SELECT COUNT(p) FROM Publication p WHERE p.isHidden = false"
    ),
    @NamedQuery(
        name = "Publication.findAll", 
        query = "SELECT p FROM Publication p LEFT JOIN FETCH p.submitter WHERE p.isHidden = false ORDER BY p.publicationDate DESC"
    ),
    @NamedQuery(
            name = "Publication.findAllAdmin",
            query = "SELECT p FROM Publication p LEFT JOIN FETCH p.submitter ORDER BY p.publicationDate DESC"
    ),
    @NamedQuery(
        name = "Publication.findByTag", 
        query = "SELECT p FROM Publication p JOIN p.tags t WHERE t.id = :tagId AND p.isHidden = false ORDER BY p.publicationDate DESC"
    ),
    @NamedQuery(
        name = "Publication.findBySubmitter", 
        query = "SELECT p FROM Publication p WHERE p.submitter.id = :submitterId ORDER BY p.publicationDate DESC"
    ),
    @NamedQuery(
        name = "Publication.countBySubmitter",
        query = "SELECT COUNT(p) FROM Publication p WHERE p.submitter.id = :submitterId"
    )
})
public class Publication implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.AUTO) private long id;
    @ManyToOne @NonNull private User submitter;
    @NonNull private String title;

    @ElementCollection(fetch = FetchType.EAGER)
    @NonNull private List<String> authors;
    private Date publicationDate = new Date();
    @NonNull private String attachedFile;
    @NonNull private FileType type;
    @JsonProperty("abstract")
    @NonNull private String abstractText;
    private boolean isHidden = false;
    private long rating = 0;
    @NonNull 
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "publications_tags",
        joinColumns = @JoinColumn(name = "publication_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;
    @OneToMany(mappedBy = "publication") List<Comment> comments = new ArrayList<>();
    @OneToMany(mappedBy = "publication", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PublicationVote> votes = new ArrayList<>();

    public int getScore() {
        int score = 0;
        for (PublicationVote vote : votes) {
            if (vote.getVoteType() == VoteType.UP) score++;
            else if (vote.getVoteType() == VoteType.DOWN) score--;
        }
        return score;
    }

    public void addTag(Tag tag) {
        if (!tags.contains(tag)) {
            tags.add(tag);
            tag.getPublications().add(this);
        }
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setPublication(this);
    }
}
