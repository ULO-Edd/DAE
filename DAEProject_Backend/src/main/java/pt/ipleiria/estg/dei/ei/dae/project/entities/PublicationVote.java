package pt.ipleiria.estg.dei.ei.dae.project.entities;

import jakarta.persistence.*;
import pt.ipleiria.estg.dei.ei.dae.project.enums.VoteType;
import java.io.Serializable;

@Entity
@Table(name = "publication_votes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"publication_id", "voter_id"}) // Um user só vota 1x por post
})
@NamedQueries({
    @NamedQuery(name = "getVote", query = "SELECT v FROM PublicationVote v WHERE v.publication.id = :pubId AND v.voter.id = :userId")
})
public class PublicationVote implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "publication_id", nullable = false)
    private Publication publication;

    @ManyToOne
    @JoinColumn(name = "voter_id", nullable = false)
    private User voter;

    @Enumerated(EnumType.STRING)
    @Column(name = "vote_type", nullable = false)
    private VoteType voteType;

    public PublicationVote() {}

    public PublicationVote(Publication publication, User voter, VoteType voteType) {
        this.publication = publication;
        this.voter = voter;
        this.voteType = voteType;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public Publication getPublication() { return publication; }
    public void setPublication(Publication publication) { this.publication = publication; }
    public User getVoter() { return voter; }
    public void setVoter(User voter) { this.voter = voter; }
    public VoteType getVoteType() { return voteType; }
    public void setVoteType(VoteType voteType) { this.voteType = voteType; }
}
