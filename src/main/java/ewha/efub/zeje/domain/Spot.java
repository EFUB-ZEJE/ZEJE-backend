package ewha.efub.zeje.domain;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Spot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long spotId;

    @Column
    private Long contentId;

    @Column(length = 45)
    @NotNull
    private String category;

    @Column(length = 45)
    @NotNull
    private String name;

    @Column(length = 255)
    @NotNull
    private String location;

    @Column
    private String description;

    @Column(length = 255)
    private String link;
}
