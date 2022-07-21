package ewha.efub.zeje.controller;

import ewha.efub.zeje.domain.Review;
import ewha.efub.zeje.dto.ReviewRequestDTO;
import ewha.efub.zeje.dto.ReviewResponseDTO;
import ewha.efub.zeje.service.ReviewService;
import ewha.efub.zeje.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponseDTO reviewSave(@RequestParam(value = "image", required = false) MultipartFile image,
                                        @RequestParam(value = "spotId") Long spotId, @RequestParam(value = "score") Integer score,
                                        @RequestParam(value = "content") String content) throws IOException {

        Long userId = userService.findSessionUser();
        ReviewRequestDTO reviewRequestDTO = ReviewRequestDTO.builder()
                .userId(userId)
                .spotId(spotId)
                .content(content)
                .score(score)
                .build();

        return reviewService.addReview(reviewRequestDTO, image);
    }

    @GetMapping
    public List<ReviewResponseDTO> reviewList() {
        return reviewService.findAllReview();
    }

    @GetMapping("/spots/{spotId}")
    public List<ReviewResponseDTO> reviewSpotList(@PathVariable Long spotId) {
        return reviewService.findReviewsBySpot(spotId);
    }

    @GetMapping("/{reviewId}")
    public ReviewResponseDTO reviewSearchById(@PathVariable Long reviewId) {
        return reviewService.findReviewById(reviewId);
    }

    @DeleteMapping("/{reviewId}")
    public String reviewDelete(@PathVariable Long reviewId) {
        return reviewService.deleteReview(reviewId);
    }
}
