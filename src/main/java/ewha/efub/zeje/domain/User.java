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

    @Column
    @NotNull
    private Long kakaoId;

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
    public User(Long kakaoId, String nickname, String profileUrl, String email) {
        this.kakaoId = kakaoId;
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

    public void updateFruitBox(Boolean add, Integer fruitBox) {
        if(add) this.fruitBox += fruitBox;
        else this.fruitBox -= fruitBox;
    }
}
