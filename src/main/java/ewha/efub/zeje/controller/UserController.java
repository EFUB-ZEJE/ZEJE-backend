package ewha.efub.zeje.controller;

import ewha.efub.zeje.dto.SessionUserDTO;
import ewha.efub.zeje.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final HttpServletRequest httpServletRequest;


    @GetMapping("/currentUser")
    public String currentUser() {
        HttpSession httpsession = httpServletRequest.getSession();
        SessionUserDTO user = (SessionUserDTO) httpsession.getAttribute("user");
        if(user != null) {
            String response = user.getEmail();
            return response;
        }
        else {
            String fail = "fail";
            return fail;
        }
    }

}
