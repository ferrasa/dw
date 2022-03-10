package dw.secauth;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class CognitoOidcLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    private final String logoutURL;
    private final String clientId;
    private final String redirectURI;

    public CognitoOidcLogoutSuccessHandler(String logoutURL, String redirectURI, String clientId) {
        this.logoutURL = logoutURL;
        this.redirectURI = redirectURI;
        this.clientId = clientId;
    }

    /*
     * GET https://mydomain.auth.us-east-1.amazoncognito.com/logout?
     * response_type=code&
     * client_id=ad398u21ijw3s9w3939&
     * redirect_uri=https://YOUR_APP/redirect_uri&
     * state=STATE&
     * scope=openid+profile+aws.cognito.signin.user.admin
     */

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) {

        UriComponents baseUrl = UriComponentsBuilder
                .fromHttpUrl(UrlUtils.buildFullRequestUrl(request))
                .replacePath(request.getContextPath())
                .replaceQuery(null)
                .fragment(null)
                .build();

        String uri = UriComponentsBuilder
                .fromUri(URI.create(logoutURL))
                .queryParam("response_type", "code")
                .queryParam("client_id", clientId)
                .queryParam("logout_uri", baseUrl)
                .queryParam("redirect_uri", redirectURI)
                .queryParam("scope", "openid")
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUriString();

        return uri;

    }
}