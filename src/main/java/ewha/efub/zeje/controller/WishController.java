package ewha.efub.zeje.controller;

import ewha.efub.zeje.config.LoginUser;
import ewha.efub.zeje.dto.SpotDTO;
import ewha.efub.zeje.dto.WishDTO;
import ewha.efub.zeje.dto.security.SessionUserDTO;
import ewha.efub.zeje.service.WishService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/wish")
public class WishController {
    private final WishService wishService;

    @GetMapping()
    public List<WishDTO> wishList(@LoginUser SessionUserDTO sessionUser){
        return wishService.findWishList(sessionUser.getUserId());
    }

    @PostMapping(value="/{spotId}")
    public String wishAdd(@LoginUser SessionUserDTO sessionUser, @PathVariable Long spotId){
        return wishService.addWish(sessionUser.getUserId(), spotId);
    }

    @DeleteMapping(value="/{spotId}")
    public String wishRemove(@LoginUser SessionUserDTO sessionUser, @PathVariable Long spotId){
        return wishService.removeWish(sessionUser.getUserId(), spotId);
    }
}
