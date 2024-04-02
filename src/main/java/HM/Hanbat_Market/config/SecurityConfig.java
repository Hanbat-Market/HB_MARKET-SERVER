package HM.Hanbat_Market.config;

import HM.Hanbat_Market.domain.entity.Role;
import HM.Hanbat_Market.service.account.CustomAuthenticationSuccessHandler;
import HM.Hanbat_Market.service.account.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomAuthenticationSuccessHandler customSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(
                        (csrfConfig) -> csrfConfig.disable()
                )
                .headers(
                        (headerConfig) -> headerConfig.frameOptions(
                                frameOptionsConfig -> frameOptionsConfig.disable()
                        )
                )
//                .authorizeHttpRequests((authorizeRequest) -> authorizeRequest
//                        .requestMatchers("/api/members/new", "/api/login", "/api/members/login", "/api/members/logout",
//                                "/css/**", "/assets/**", "/files/**", "/api/images/**", "/*.ico", "/error", "/swagger-ui/**", "/swagger-resources/**",
//                                "/v3/api-docs/**", "/api-docs/**", "/swagger-ui.html").permitAll()
//                        .anyRequest().authenticated()
//                )
                .logout( // 로그아웃 성공 시 / 주소로 이동
                        (logoutConfig) -> logoutConfig.logoutSuccessUrl("/")
                )
                // OAuth2 로그인 기능에 대한 여러 설정
                .oauth2Login(Customizer.withDefaults());
        // 아래 코드와 동일한 결과
        /*
                .oauth2Login(
                        (oauth) ->
                            oauth.userInfoEndpoint(
                                    (endpoint) -> endpoint.userService(customOAuth2UserService)
                            )
                );
        */

        return http.build();
    }
}