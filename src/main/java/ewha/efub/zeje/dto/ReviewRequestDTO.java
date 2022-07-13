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
    public ReviewRequestDTO(Review review) {
        this.spotId = review.getSpot().getSpotId();
        this.userId = review.getUser().getUserId();
        this.content = review.getContent();
        this.score = review.getScore();
    }
}

