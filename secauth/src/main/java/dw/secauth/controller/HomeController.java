package dw.secauth.controller;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(Authentication a) {
        if (a != null)
            return "index.html";
        return "redirect:oauth2/authorization/cognito";
    }
    
}
