package ewha.efub.zeje.service;

import ewha.efub.zeje.domain.*;
import ewha.efub.zeje.dto.ReviewRequestDTO;
import ewha.efub.zeje.dto.ReviewResponseDTO;
import ewha.efub.zeje.util.errors.CustomException;
import ewha.efub.zeje.util.errors.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final SpotRepository spotRepository;
    private final ImageUploadService imageUploadService;

    public ReviewResponseDTO addReview(ReviewRequestDTO reviewRequestDTO, MultipartFile imageFile) throws IOException {
        User user = userRepository.findById(reviewRequestDTO.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Spot spot = spotRepository.findById(reviewRequestDTO.getSpotId())
                .orElseThrow(() -> new CustomException(ErrorCode.SPOT_NOT_FOUND));

        if(imageFile != null) {
            String fileUrl = imageUploadService.uploadImage(2, imageFile);
            if(fileUrl.equals("type error") || fileUrl.equals("null error")) {
                throw new CustomException(ErrorCode.INVALID_IMAGE_FILE);
            }

            return new ReviewResponseDTO(saveReview(reviewRequestDTO, fileUrl, user, spot));
        }
        else {
            return new ReviewResponseDTO(saveReview(reviewRequestDTO, user, spot));
        }

    }

    public Review saveReview(ReviewRequestDTO reviewRequestDTO, String fileUrl, User user, Spot spot) {
        Review review = Review.builder()
                .content(reviewRequestDTO.getContent())
                .user(user)
                .spot(spot)
                .score(reviewRequestDTO.getScore())
                .image(fileUrl)
                .build();

        return reviewRepository.save(review);
    }

    public Review saveReview(ReviewRequestDTO reviewRequestDTO, User user, Spot spot) {
        Review review = Review.builder()
                .content(reviewRequestDTO.getContent())
                .user(user)
                .spot(spot)
                .score(reviewRequestDTO.getScore())
                .build();

        return reviewRepository.save(review);
    }

    public ReviewResponseDTO findReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        return new ReviewResponseDTO(review);
    }

    public List<ReviewResponseDTO> findReviewsBySpot(Long spotId) {
        Spot spot = spotRepository.findById(spotId)
                .orElseThrow(() -> new CustomException(ErrorCode.SPOT_NOT_FOUND));

        return reviewRepository.findAllBySpot(spot)
                .stream()
                .map(review -> new ReviewResponseDTO(review))
                .collect(Collectors.toList());
    }

    public List<ReviewResponseDTO> findAllReview() {
        return reviewRepository.findAll()
                .stream()
                .map(review -> new ReviewResponseDTO(review))
                .collect(Collectors.toList());
    }

    public String deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        reviewRepository.delete(review);
        return "SUCCESS";
    }
}
