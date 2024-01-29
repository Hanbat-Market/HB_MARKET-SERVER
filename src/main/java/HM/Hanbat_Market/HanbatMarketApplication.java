package HM.Hanbat_Market;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing //시간 감시
@SpringBootApplication
public class HanbatMarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(HanbatMarketApplication.class, args);
	}

}
