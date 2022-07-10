package ewha.efub.zeje.domain;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "donation")
public class Donation extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long donationId;

    @NotNull
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @NotNull
    @Column
    private Integer fruit;

    @NotNull
    @Column
    private Boolean donationFlag;

    @Builder
    public Donation(User user, Integer fruit) {
        this.user = user;
        this.fruit = fruit;
        this.donationFlag = false;
    }

    public void updateFruit(Integer fruit) {
        this.fruit += fruit;
    }

}
