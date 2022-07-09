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

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final HttpServletRequest httpServletRequest;
    private final UserService userService;


    @GetMapping("/currentUser")
    public String userCurrent() {
        HttpSession httpsession = httpServletRequest.getSession();
        SessionUserDTO user = (SessionUserDTO) httpsession.getAttribute("user");
        if(user != null) {
            String response = user.getUserId().toString();
            return response;
        }
        else {
            String fail = "fail";
            return fail;
        }
    }

    @GetMapping("/{userId}")
    public UserResponseDTO userDetails(@PathVariable Long userId) {
        UserResponseDTO userDTO = userService.findUser(userId);
        return userDTO;
    }

    @DeleteMapping ("/{userId}")
    public String userRemove(@PathVariable Long userId) {
        boolean success = userService.removeUser(userId);
        if(success) {
            return userId.toString() + "번 유저 탈퇴처리 완료";
        }
        else {
            return "탈퇴실패";
        }
    }

    @PatchMapping("/{userId}/image")
    public UserResponseDTO updateProfileByNum(@PathVariable Long userId, @RequestParam(value="nickname", required = false) String nickname, @RequestParam(value="uploadFile", required = false) MultipartFile uploadFile) throws IOException {
        return userService.updateProfile(userId, nickname, uploadFile);
    }
}
