package HM.Hanbat_Market.config;

import HM.Hanbat_Market.repository.member.MemberRepository;
import HM.Hanbat_Market.service.account.CustomOAuth2UserService;
import HM.Hanbat_Market.service.account.CustomSuccessHandler;
import HM.Hanbat_Market.service.account.RefreshTokenRepository;
import HM.Hanbat_Market.service.account.RefreshTokenService;
import HM.Hanbat_Market.service.account.jwt.CustomAuthenticationEntryPoint;
import HM.Hanbat_Market.service.account.jwt.JWTFilter;
import HM.Hanbat_Market.service.account.jwt.JWTUtil;
import HM.Hanbat_Market.service.member.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JWTUtil jwtUtil;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final RefreshTokenService refreshTokenService;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, CustomSuccessHandler customSuccessHandler,
                          JWTUtil jwtUtil,
                          MemberService memberService, MemberRepository memberRepository,
                          RefreshTokenService refreshTokenService,
                          CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {

        this.customOAuth2UserService = customOAuth2UserService;
        this.customSuccessHandler = customSuccessHandler;
        this.jwtUtil = jwtUtil;
        this.memberService = memberService;
        this.memberRepository = memberRepository;
        this.refreshTokenService = refreshTokenService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // csrf disable
        http
                .csrf((auth) -> auth.disable());

        // From 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        // HTTP Basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());

        // JWTFilter 추가
        http
                .addFilterBefore(new JWTFilter(jwtUtil, memberService), UsernamePasswordAuthenticationFilter.class);

        // oauth2
        http
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                .userService(customOAuth2UserService))
                        .successHandler(customSuccessHandler)
                );

        // 경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(
                                "/api/members/new", "/api/login", "/api/members/login", "/api/members/logout",
                                "/css/**", "/assets/**", "/files/**", "/api/images/**", "/*.ico", "/error",
                                "/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**", "/api-docs/**",
                                "/swagger-ui.html",
                                "/google79674106d1aa552b.html", "/mentoring/room/**", "/chat/**",
                                "/chat-front/chat.html",
                                "/chat-front/**", "/api/fcm", "/api/fcm/save", "/api/verification",
                                "/api/verification/match", "/api/verification/confirm", "/apple", "/apple/login",
                                "/redirect", "/refresh", "/apps/to/endpoint", "/clear", "/api/account/refresh")
                        .permitAll()
                        .anyRequest().authenticated());

        // 세션 설정 : STATELESS
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        return http.build();
    }
}