package HM.Hanbat_Market.aop.filter;

import HM.Hanbat_Market.aop.LoggingFilter;
import HM.Hanbat_Market.aop.QueryCountInspector;
import HM.Hanbat_Market.aop.Timer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public LoggingFilter loggingFilter(Timer timer,QueryCountInspector queryCountInspector) {
        return new LoggingFilter(timer, queryCountInspector);
    }
}