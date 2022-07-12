package ewha.efub.zeje.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
public class UserRequestDTO {
    private String nickname;

    @Builder
    public UserRequestDTO(String nickname) {
        this.nickname = nickname;
    }
}
