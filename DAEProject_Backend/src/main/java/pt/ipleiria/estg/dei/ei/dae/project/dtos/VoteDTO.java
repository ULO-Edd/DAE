package pt.ipleiria.estg.dei.ei.dae.project.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public class VoteDTO implements Serializable {
    
    @JsonProperty("vote_type") 
    private String voteType;

    public VoteDTO() {
    }

    public VoteDTO(String voteType) {
        this.voteType = voteType;
    }

    public String getVoteType() { return voteType; }
    public void setVoteType(String voteType) { this.voteType = voteType; }
}
