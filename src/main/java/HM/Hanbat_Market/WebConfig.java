package HM.Hanbat_Market;

import HM.Hanbat_Market.api.member.login.LoginMemberArgumentResolver;
import HM.Hanbat_Market.api.member.login.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/members/new", "/api/login", "/api/members/login", "/api/members/logout",
                        "/css/**", "/assets/**", "/Hanbat_Market_File/**", "/*.ico", "/error");
    }
}

