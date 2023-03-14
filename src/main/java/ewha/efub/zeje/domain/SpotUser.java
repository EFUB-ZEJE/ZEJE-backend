package ewha.efub.zeje.domain;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Table(name = "spot_user")
@Entity
public class SpotUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long spotUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot_id")
    private Spot spot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public SpotUser(Spot spot, User user) {
        this.spot = spot;
        this.user = user;
    }
}
