package dw.secauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Value("${spring.security.oauth2.client.registration.cognito.clientId}")
    private  String clientId;
    
    @Value("${app.cognito.logoutURL}")
    private String logoutURL;

    @Value("${app.cognito.redirectURI}")
    private String redirectURI;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(Customizer.withDefaults())
            .authorizeHttpRequests(authz -> authz.requestMatchers("/")
                .permitAll()
                .anyRequest()
                .authenticated())
            .oauth2Login(Customizer.withDefaults())
			.logout(httpSecurityLogoutConfigurer -> {
                httpSecurityLogoutConfigurer.logoutSuccessHandler(
                    new CustomLogoutHandler(logoutURL, redirectURI, clientId));
                });
        return http.build();
    }


    
}
