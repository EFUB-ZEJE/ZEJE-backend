package ewha.efub.zeje.domain;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "review")
public class Review extends TimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot_id")
    private Spot spot;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @Column
    private String content;

    @NotNull
    @Column
    private Integer score;

    @Column
    private String image;

    @Builder
    public Review(Spot spot, User user, String content, Integer score, String image){
        this.spot = spot;
        this.user = user;
        this.content = content;
        this.score = score;
        this.image = image;
    }
}
