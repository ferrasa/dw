package dw.secauth.controller;

//import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
class HomeController {

    /*  Obs.: retirando os comentários do código, ao fazer o logout 
     *      você será redirecionado para a tela de login novamente.
     */
    @GetMapping("/")
    String index(/*Authentication a*/) {
      //  if (a != null)
            return "index.html";
        //return "redirect:oauth2/authorization/cognito";
    }    
}
