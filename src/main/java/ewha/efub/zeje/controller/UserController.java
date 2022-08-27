package ewha.efub.zeje.controller;

import ewha.efub.zeje.domain.User;
import ewha.efub.zeje.dto.FruitRequestDTO;
import ewha.efub.zeje.dto.security.SessionUserDTO;
import ewha.efub.zeje.dto.user.UserResponseDTO;
import ewha.efub.zeje.service.JwtTokenProvider;
import ewha.efub.zeje.service.OAuthUserService;
import ewha.efub.zeje.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

import static ewha.efub.zeje.dto.user.UserResponseDTO.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/login")
    public String userSave(HttpServletRequest request) {
        String kakaoToken = request.getHeader("Authorization");
        User user = userService.saveUser(kakaoToken);
        String token = jwtTokenProvider.createJwtAccessToken(user.getKakaoId().toString(), user.getNickname());
        return token;
    }

    @GetMapping("/account/profile")
    public UserResponseDTO userDetails(HttpServletRequest request) {
        SessionUserDTO sessionUser = jwtTokenProvider.getUserInfoByToken(request);
        UserResponseDTO userDTO = userService.findUser(sessionUser.getUserId());
        return userDTO;
    }

    @DeleteMapping ("/account")
    public String userRemove(HttpServletRequest request) {
        SessionUserDTO sessionUser = jwtTokenProvider.getUserInfoByToken(request);
        boolean success = userService.removeUser(sessionUser.getUserId());
        if(success) {
            return "유저 탈퇴처리 완료";
        }
        else {
            return "탈퇴실패";
        }
    }

    @PatchMapping("/account/profile")
    public UserResponseDTO userModify(HttpServletRequest request,
                                      @RequestParam(value="nickname", required = false) String nickname,
                                      @RequestParam(value="uploadFile", required = false) MultipartFile uploadFile) throws IOException {
        SessionUserDTO sessionUser = jwtTokenProvider.getUserInfoByToken(request);
        return userService.updateProfile(sessionUser.getUserId(), nickname, uploadFile);
    }

    @GetMapping("/account/profile/fruitBox")
    public UserFruitResponseDTO fruitDetails(HttpServletRequest request) {
        SessionUserDTO sessionUser = jwtTokenProvider.getUserInfoByToken(request);
        return userService.findFruitBox(sessionUser.getUserId());
    }

    @PostMapping("/account/profile/fruitBox")
    public UserFruitResponseDTO fruitModify(HttpServletRequest request, @RequestBody FruitRequestDTO fruitRequestDTO) {
        SessionUserDTO sessionUser = jwtTokenProvider.getUserInfoByToken(request);
        return userService.modifyFruitBoxAdd(sessionUser.getUserId(), fruitRequestDTO);
    }
}
