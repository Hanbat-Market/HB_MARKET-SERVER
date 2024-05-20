package HM.Hanbat_Market;

import HM.Hanbat_Market.service.account.apple.common.apple.GetMemberInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class HanbatMarketApplicationTests {

	@Autowired
	GetMemberInfoService getMemberInfoService;

	@Test
	void getToken() {
		String authorizationCode = "클라이언트로 부터 받은 애플 인가코드";

		var source = getMemberInfoService.get(authorizationCode);
	}

	@Test
	void contextLoads() {
	}

}
