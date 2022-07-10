package ewha.efub.zeje.controller;

import ewha.efub.zeje.dto.SessionUserDTO;
import ewha.efub.zeje.dto.UserRequestDTO;
import ewha.efub.zeje.dto.UserResponseDTO;
import ewha.efub.zeje.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static ewha.efub.zeje.dto.UserRequestDTO.*;
import static ewha.efub.zeje.dto.UserResponseDTO.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/account/profile")
    public UserResponseDTO userDetails() {
        Long userId = userService.findSessionUser();
        UserResponseDTO userDTO = userService.findUser(userId);
        return userDTO;
    }

    @DeleteMapping ("/account")
    public String userRemove() {
        Long userId = userService.findSessionUser();
        boolean success = userService.removeUser(userId);
        if(success) {
            return userId.toString() + "번 유저 탈퇴처리 완료";
        }
        else {
            return "탈퇴실패";
        }
    }

    @PatchMapping("/account/profile")
    public UserResponseDTO userModify(@RequestParam(value="nickname", required = false) String nickname, @RequestParam(value="uploadFile", required = false) MultipartFile uploadFile) throws IOException {
        Long userId = userService.findSessionUser();
        return userService.updateProfile(userId, nickname, uploadFile);
    }

    @GetMapping("/account/profile/fruitBox")
    public UserFruitResponseDTO fruitDetails() {
        Long userId = userService.findSessionUser();
        return userService.findFruitBox(userId);
    }

    @PostMapping("/account/profile/fruitBox")
    public UserFruitResponseDTO fruitModify(@RequestBody FruitRequestDTO fruitRequestDTO) {
        Long userId = userService.findSessionUser();
        return userService.modifyFruitBoxAdd(userId, fruitRequestDTO);
    }
}
