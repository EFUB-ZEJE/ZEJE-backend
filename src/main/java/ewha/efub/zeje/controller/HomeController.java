package ewha.efub.zeje.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/main")
    public String wssTest() {
        return "login";
    }

    @GetMapping("/fruitTest")
    public String fruitTest() {
        return "fruitTest";
    }

}
