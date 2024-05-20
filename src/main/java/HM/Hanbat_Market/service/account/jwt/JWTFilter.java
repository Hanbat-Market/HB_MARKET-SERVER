package HM.Hanbat_Market.service.account.jwt;

import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.domain.entity.LoginStatus;
import HM.Hanbat_Market.domain.entity.Role;
import HM.Hanbat_Market.service.member.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final MemberService memberService;

    public JWTFilter(JWTUtil jwtUtil, MemberService memberService) {

        this.jwtUtil = jwtUtil;
        this.memberService = memberService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 스웨거 관련 경로 리스트
        List<String> swaggerPaths = Arrays.asList("/css/", "/assets/", "/files/", "/api/images/", "/favicon.ico",
                "/error", "/swagger-ui/", "/swagger-resources/",
                "/v3/api-docs", "/api-docs", "/swagger-ui.html", "/google79674106d1aa552b.html", "/chat",
                "/chat-front/chat.html",
                "/api/fcm", "/api/fcm/save", "/api/verification", "/api/verification/match",
                "/api/verification/confirm", "/apple", "/apple/login", "/redirect", "/refresh",
                "/apps/to/endpoint", "/clear");

        // 요청된 경로
        String path = request.getRequestURI();

        // 스웨거 관련 경로 또는 다른 허용된 경로인 경우 체크
        boolean isSwaggerPath = swaggerPaths.stream()
                .anyMatch(swaggerPath -> path.startsWith(request.getContextPath() + swaggerPath));

        // 스웨거 관련 경로 또는 다른 허용된 경로인 경우, 필터 체인을 계속 진행
        if (isSwaggerPath) {
            filterChain.doFilter(request, response);
            return; // 필터링 종료
        }

        //cookie들을 불러온 뒤 Authorization Key에 담긴 쿠키를 찾음
        String authorization = null;

        try {
            log.info(request.getCookies().toString());
            log.info("@@@@@@@@@@@@@@@@@@@@");
            Cookie[] asd = request.getCookies();

            for (Cookie cookie : asd) {

                System.out.println(cookie.getName());
                System.out.println(cookie.getValue());
            }

            Cookie[] cookies = request.getCookies();

            for (Cookie cookie : cookies) {

                System.out.println(cookie.getName());
                if (cookie.getName().equals("Authorization")) {
                    authorization = cookie.getValue();
                }
            }

        } catch (NullPointerException e) {
//            throw new TokenNotValidException();
            return;
        }

        //Authorization 헤더 검증
        if (authorization == null) {

            System.out.println("token null@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            System.out.println("token null@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            System.out.println("token null@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        //토큰
        String token = authorization;

        //토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token)) {

            System.out.println("token expired");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        //토큰에서 username과 role 획득
        String mail = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        Member member = Member.createMember("tmp", "tmp", "123", mail, Role.USER);

        //UserDetails에 회원 정보 객체 담기
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(member);

        Member findMember = memberService.findByMail(mail).get();

        if (findMember.getLoginStatus() != LoginStatus.LOGIN) {
            memberService.login(findMember.getId());
            log.info("@@@@@@@@@@@ login 상태 변경");
        }

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null,
                customOAuth2User.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}