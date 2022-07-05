package ewha.efub.zeje.domain;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "spot")
public class Spot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long spotId;

    @NotNull
    @Column(name = "content_id")
    private Long contentId;

    @NotNull
    @Column(length = 45)
    private String category;

    @Column(length = 50)
    private String type;

    @NotNull
    @Column(length = 45)
    private String name;

    @NotNull
    @Column
    private String location;

    @Column
    private String description;

    @Column
    private String link;

    @Builder
    public Spot(Long contentId, String category, String type, String name, String location) {
        this.contentId = contentId;
        this.category = category;
        this.type = type;
        this.name = name;
        this.location = location;
    }
}


