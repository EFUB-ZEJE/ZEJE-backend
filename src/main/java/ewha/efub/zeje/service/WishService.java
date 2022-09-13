package ewha.efub.zeje.service;

import ewha.efub.zeje.domain.*;
import ewha.efub.zeje.dto.SpotDTO;
import ewha.efub.zeje.dto.WishDTO;
import ewha.efub.zeje.util.errors.CustomException;
import ewha.efub.zeje.util.errors.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class WishService {
    private final WishRepository wishRepository;
    private final UserRepository userRepository;
    private final SpotRepository spotRepository;

    public List<WishDTO> findWishList(Long userId){
        return wishRepository.findByUser_UserId(userId)
                .stream()
                .map(WishDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public String addWish(Long userId, Long spotId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Spot spot = spotRepository.findById(spotId)
                .orElseThrow(() -> new CustomException(ErrorCode.SPOT_NOT_FOUND));
        Wish wish = Wish.builder()
                .user(user)
                .spot(spot)
                .build();
        wishRepository.save(wish);
        spot.plusWishCount();

        return wish.getWishId().toString();
    }

    @Transactional
    public String removeWish(Long userId, Long spotId){
        Long wishId = wishRepository.findByUser_UserIdAndSpot_SpotId(userId, spotId)
                .orElseThrow(() -> new CustomException(ErrorCode.WISH_NOT_FOUND))
                .getWishId();
        wishRepository.deleteById(wishId);

        Spot spot = spotRepository.findById(spotId)
                .orElseThrow(() -> new CustomException(ErrorCode.SPOT_NOT_FOUND));

        if(spot.getWishCount() > 0) {
            spot.minusWishCount();
        }

        return wishId.toString()+"번 위시리스트 삭제 완료";
    }
}
