package ewha.efub.zeje.domain;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "user")
public class User extends TimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(length = 45)
    @NotNull
    private String nickname;

    @Column
    private String profileUrl;

    @Column
    @NotNull
    private String email;

    @Column
    @NotNull
    private Integer fruitBox;

    @Column
    @NotNull
    private Boolean deleteFlag;

    @Builder
    public User(String nickname, String profileUrl, String email) {
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        this.email = email;
        this.fruitBox = 0;
        this.deleteFlag = false;
    }

    public void deleteUser() {
        this.deleteFlag = true;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfile(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public void updateFruitBox(Integer fruitBox) {
        this.fruitBox += fruitBox;
    }
}
