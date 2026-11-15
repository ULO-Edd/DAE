package pt.ipleiria.estg.dei.ei.dae.project.dtos;

import jakarta.json.bind.annotation.JsonbProperty;
import java.io.Serializable;

public class VoteResponseDTO implements Serializable {
    private int score;
    
    @JsonbProperty("my_vote")
    private String myVote;

    public VoteResponseDTO(int score, String myVote) {
        this.score = score;
        this.myVote = myVote;
    }
    
    public int getScore() { return score; }
    public String getMyVote() { return myVote; }
}
