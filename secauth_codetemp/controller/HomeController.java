package dw.secauth.controller;

//import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
class HomeController {

    @GetMapping("/")
    String index(Authentication a) {
        if (a != null)
            return "index.html";
        return "redirect:oauth2/authorization/cognito";
       //return "index.html";
    }

}