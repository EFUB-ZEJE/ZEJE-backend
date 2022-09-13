package ewha.efub.zeje.dto;

import ewha.efub.zeje.domain.Review;
import ewha.efub.zeje.domain.Spot;
import ewha.efub.zeje.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReviewResponseDTO {
    private Long reviewId;
    private Long spotId;
    private Long userId;
    private String profile;
    private String nickname;
    private String content;
    private Integer score;
    private LocalDateTime createdDate;
    private String image;

    @Builder
    public ReviewResponseDTO(Review review) {
        this.reviewId = review.getReviewId();
        this.spotId = review.getSpot().getSpotId();
        this.userId = review.getUser().getUserId();
        this.profile = review.getUser().getProfileUrl();
        this.nickname = review.getUser().getNickname();
        this.content = review.getContent();
        this.score = review.getScore();
        this.createdDate = review.getCreatedDate();
        this.image = review.getImage();
    }
}
