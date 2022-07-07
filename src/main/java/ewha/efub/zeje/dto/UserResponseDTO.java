package ewha.efub.zeje.dto;

import ewha.efub.zeje.domain.User;
import lombok.Getter;

@Getter
public class UserResponseDTO {
    private Long userId;
    private String nickname;
    private String email;
    private String profileUrl;
    private Integer fruitBox;

    public UserResponseDTO(User entity) {
        this.userId = entity.getUserId();
        this.nickname = entity.getNickname();
        this.email = entity.getEmail();
        this.profileUrl = entity.getProfileUrl();
        this.fruitBox = entity.getFruitBox();
    }
}
