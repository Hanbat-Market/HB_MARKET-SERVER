package HM.Hanbat_Market.config;

import HM.Hanbat_Market.aop.QueryCountInspector;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateConfiguration {

    private final QueryCountInspector queryCountInspector;

    public HibernateConfiguration(final QueryCountInspector queryCountInspector) {
        this.queryCountInspector = queryCountInspector;
    }

    @Bean
    HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
        return properties -> properties.put(AvailableSettings.STATEMENT_INSPECTOR, queryCountInspector);
    }
}