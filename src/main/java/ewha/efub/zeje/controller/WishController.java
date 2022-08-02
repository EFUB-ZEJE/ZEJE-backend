package ewha.efub.zeje.controller;

import ewha.efub.zeje.dto.SpotDTO;
import ewha.efub.zeje.dto.WishDTO;
import ewha.efub.zeje.dto.security.SessionUserDTO;
import ewha.efub.zeje.service.JwtTokenProvider;
import ewha.efub.zeje.service.WishService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/wish")
public class WishController {
    private final WishService wishService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping()
    public List<WishDTO> wishList(HttpServletRequest request){
        SessionUserDTO sessionUser = jwtTokenProvider.getUserInfoByToken(request);
        return wishService.findWishList(sessionUser.getUserId());
    }

    @PostMapping(value="/{spotId}")
    public String wishAdd(HttpServletRequest request, @PathVariable Long spotId){
        SessionUserDTO sessionUser = jwtTokenProvider.getUserInfoByToken(request);
        return wishService.addWish(sessionUser.getUserId(), spotId);
    }

    @DeleteMapping(value="/{spotId}")
    public String wishRemove(HttpServletRequest request, @PathVariable Long spotId){
        SessionUserDTO sessionUser = jwtTokenProvider.getUserInfoByToken(request);
        return wishService.removeWish(sessionUser.getUserId(), spotId);
    }
}
