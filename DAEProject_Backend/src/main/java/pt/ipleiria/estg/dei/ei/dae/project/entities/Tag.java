package pt.ipleiria.estg.dei.ei.dae.project.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NamedQueries({
        @NamedQuery(
                name = "getAllTags",
                query = "SELECT t FROM Tag t ORDER BY t.name" // JPQL
        ),
        @NamedQuery(
                name = "getTagByName",
                query = "SELECT t FROM Tag t WHERE t.name = :name" // JPQL
        )
})

@Entity
@Table(name = "tags",
        uniqueConstraints = { @UniqueConstraint(columnNames = "name") })
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Tag extends Versionable implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.AUTO) private long id;
    @NonNull private String name;
    @ManyToMany(mappedBy = "tags")     
    private List<Publication> publications;
    @Version
    private int version;
}
