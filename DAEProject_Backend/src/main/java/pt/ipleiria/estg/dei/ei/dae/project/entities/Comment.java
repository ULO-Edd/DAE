package pt.ipleiria.estg.dei.ei.dae.project.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@NamedQueries({
        @NamedQuery(
                name = "getAllCommentsFromPost",
                query = "SELECT c FROM Comment c WHERE c.publication.id = :postId ORDER BY c.createdAt"
        ),
        @NamedQuery(
                name = "getAllCommentsFromUser",
                query = "SELECT c FROM Comment c WHERE c.author.id = :userId ORDER BY c.createdAt"
        ),
        @NamedQuery(
                name = "getCommentByUserAndPost",
                query = "SELECT c FROM Comment c WHERE c.author.id = :userId AND c.publication.id = :postId ORDER BY c.createdAt"
        )
})
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.AUTO) private long id;
    @ManyToOne @NonNull private Publication publication;
    @ManyToOne @NonNull private User author;
    @NonNull private String content;
    private Date createdAt = new Date();
    private boolean isHidden;


    public Comment(Publication publication, User author, String content, Date createdAt, boolean isHidden) {
        this.publication = publication;
        this.author = author;
        this.content = content;
        this.createdAt = createdAt;
        this.isHidden = isHidden;
    }

}
