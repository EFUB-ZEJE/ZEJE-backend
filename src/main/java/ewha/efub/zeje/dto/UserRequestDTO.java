package ewha.efub.zeje.dto;

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

    @Getter
    @NoArgsConstructor
    public static class FruitRequestDTO {
        private Integer fruitBox;

        @Builder
        public FruitRequestDTO (Integer fruitBox) {
            this.fruitBox = fruitBox;
        }
    }



}
