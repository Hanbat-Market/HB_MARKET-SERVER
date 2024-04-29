package HM.Hanbat_Market.aop;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class LoggingFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingFilter.class);

    private final Timer timer;
    private final QueryCountInspector queryCountInspector;

    public LoggingFilter(final Timer timer, final QueryCountInspector queryCountInspector) {
        this.timer = timer;
        this.queryCountInspector = queryCountInspector;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        timer.start();

        filterChain.doFilter(request, response);

        LOGGER.info("요청 : {} {}, {}ms, count: {}",
                request.getMethod(), request.getRequestURI(), timer.getLatencyMillis(), queryCountInspector.getCount());
        queryCountInspector.clear();
    }
}