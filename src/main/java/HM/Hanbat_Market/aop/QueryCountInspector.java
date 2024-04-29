package HM.Hanbat_Market.aop;

import static java.util.Objects.isNull;

import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.stereotype.Component;

@Component
public class QueryCountInspector implements StatementInspector {

    private static final ThreadLocal<Integer> COUNTER = new ThreadLocal<>();

    @Override
    public String inspect(final String sql) {
        final Integer currentCount = COUNTER.get();

        if (isNull(currentCount)) {
            COUNTER.set(1);
            return sql;
        }

        COUNTER.set(currentCount + 1);
        return sql;
    }

    public int getCount() {
        final Integer count = COUNTER.get();

        if (isNull(count)) {
            return 0;
        }
        return count;
    }

    public void clear() {
        COUNTER.remove();
    }
}