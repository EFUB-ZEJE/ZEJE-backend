package ewha.efub.zeje.service;

import ewha.efub.zeje.domain.*;
import ewha.efub.zeje.dto.SpotDTO;
import ewha.efub.zeje.dto.WishDTO;
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
        User user = userRepository.findById(userId).get();
        Spot spot = spotRepository.findById(spotId).get();
        Wish wish = Wish.builder()
                .user(userRepository.findById(userId).get())
                .spot(spotRepository.findById(spotId).get())
                .build();
        wishRepository.save(wish);
        return "success";
    }

    @Transactional
    public String removeWish(Long userId, Long spotId){
        Long wishId = wishRepository.findByUser_UserIdAndSpot_SpotId(userId, spotId)
                .get()
                .getWishId();
        wishRepository.deleteById(wishId);

        return wishId.toString()+"번 위시리스트 삭제 완료";
    }
}
