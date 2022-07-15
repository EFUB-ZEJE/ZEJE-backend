package ewha.efub.zeje.controller;

import ewha.efub.zeje.config.LoginUser;
import ewha.efub.zeje.dto.FruitRequestDTO;
import ewha.efub.zeje.dto.security.SessionUserDTO;
import ewha.efub.zeje.dto.user.UserResponseDTO;
import ewha.efub.zeje.service.OAuthUserService;
import ewha.efub.zeje.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

import static ewha.efub.zeje.dto.user.UserResponseDTO.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final OAuthUserService oAuthUserService;

    @PostMapping("/postman")
    public Object sessionTest(@RequestBody Map<String,Object> attribute) {
        return oAuthUserService.loadUserPostman(attribute);
    }

    @GetMapping("/account/profile")
    public UserResponseDTO userDetails(@LoginUser SessionUserDTO sessionUser) {
        UserResponseDTO userDTO = userService.findUser(sessionUser.getUserId());
        return userDTO;
    }

    @DeleteMapping ("/account")
    public String userRemove(@LoginUser SessionUserDTO sessionUser) {
        boolean success = userService.removeUser(sessionUser.getUserId());
        if(success) {
            return "유저 탈퇴처리 완료";
        }
        else {
            return "탈퇴실패";
        }
    }

    @PatchMapping("/account/profile")
    public UserResponseDTO userModify(@LoginUser SessionUserDTO sessionUser,
                                      @RequestParam(value="nickname", required = false) String nickname,
                                      @RequestParam(value="uploadFile", required = false) MultipartFile uploadFile) throws IOException {
        return userService.updateProfile(sessionUser.getUserId(), nickname, uploadFile);
    }

    @GetMapping("/account/profile/fruitBox")
    public UserFruitResponseDTO fruitDetails(@LoginUser SessionUserDTO sessionUser) {
        return userService.findFruitBox(sessionUser.getUserId());
    }

    @PostMapping("/account/profile/fruitBox")
    public UserFruitResponseDTO fruitModify(@LoginUser SessionUserDTO sessionUser, @RequestBody FruitRequestDTO fruitRequestDTO) {
        return userService.modifyFruitBoxAdd(sessionUser.getUserId(), fruitRequestDTO);
    }
}
