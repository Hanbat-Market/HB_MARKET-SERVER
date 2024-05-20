package HM.Hanbat_Market.service.account.apple.apple.service;

import HM.Hanbat_Market.api.Result;
import HM.Hanbat_Market.service.account.apple.apple.model.TokenResponse;

import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Map;

public interface AppleService {

    String getAppleClientSecret(String id_token) throws NoSuchAlgorithmException;

    Result requestCodeValidations(String client_secret, String code, String refresh_token, HttpServletResponse response)
            throws IOException, ParseException, JOSEException;

    Map<String, String> getLoginMetaInfo();

    String getPayload(String id_token);

}
