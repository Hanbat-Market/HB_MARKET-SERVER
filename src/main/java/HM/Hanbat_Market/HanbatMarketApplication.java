package HM.Hanbat_Market;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableJpaAuditing //시간 감시
@SpringBootApplication
@EnableReactiveMongoRepositories
public class HanbatMarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(HanbatMarketApplication.class, args);
	}
}
