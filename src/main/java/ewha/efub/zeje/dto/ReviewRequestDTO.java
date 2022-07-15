package ewha.efub.zeje.dto;

import ewha.efub.zeje.domain.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewRequestDTO {
    private Long spotId;
    private Long userId;
    private String content;
    private Integer score;

    @Builder
    public ReviewRequestDTO(Long spotId, Long userId, String content, Integer score) {
        this.spotId = spotId;
        this.userId = userId;
        this.content = content;
        this.score = score;
    }
}

