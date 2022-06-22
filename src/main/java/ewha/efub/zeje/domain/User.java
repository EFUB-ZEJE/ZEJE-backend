package ewha.efub.zeje.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class User extends TimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column
    private String nickname;

    @Column
    private String profileUrl;

    @Column
    private String email;

    @Column
    private Integer fruitBox;

    @Column
    private Boolean deleteFlag;

    @Builder
    public User(String nickname, String profileUrl, String email) {
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        this.email = email;
        this.fruitBox = 0;
        this.deleteFlag = false;
    }

}
