package dw.secauth;

import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecController {

    @GetMapping("/oauthinfo")
    public String oathUserInfo(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient c,
            @AuthenticationPrincipal OAuth2User o){

        String r = "User name: "+o.getName() + "<br/>"+
                    "User auth: "+o.getAuthorities() + "<br/>"+
                    "Client name: "+c.getClientRegistration().getClientName()+ "<br/>"+
                    this.list(o.getAttributes());
        return r;
    }

    private String list(Map<String, Object> atributos)
    {
        String acc = "User attributes: <br/><div style='padding-left:20px'>";

        for (String key : atributos.keySet()){
            Object o = atributos.get(key);
            acc += "<div>"+ key + ":&nbsp" + o.toString() + "<div>";
        }
        return acc + "</div>";
    }


    
}
