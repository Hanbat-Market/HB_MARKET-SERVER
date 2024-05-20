package HM.Hanbat_Market.service.account.apple.apple.service;

import HM.Hanbat_Market.api.Result;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.domain.entity.Role;
import HM.Hanbat_Market.repository.member.MemberRepository;
import HM.Hanbat_Market.service.account.OAuthAttributes;
import HM.Hanbat_Market.service.account.apple.apple.model.TokenResponse;
import HM.Hanbat_Market.service.account.apple.apple.utils.AppleUtils;

import HM.Hanbat_Market.service.account.jwt.JWTUtil;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AppleServiceImpl implements AppleService {

    private final AppleUtils appleUtils;



    /**
     * 유효한 id_token인 경우 client_secret 생성
     *
     * @param id_token
     * @return
     */
    @Override
    public String getAppleClientSecret(String id_token) throws NoSuchAlgorithmException {

        if (appleUtils.verifyIdentityToken(id_token)) {
            return appleUtils.createClientSecret();
        }

        return null;
    }

    /**
     * code 또는 refresh_token가 유효한지 Apple Server에 검증 요청
     *
     * @param client_secret
     * @param code
     * @param refresh_token
     * @return
     */
    @Override
    public Result requestCodeValidations(String client_secret, String code, String refresh_token, HttpServletResponse response)
            throws IOException, ParseException, JOSEException {

        Result result = null;

        if (client_secret != null && code != null && refresh_token == null) {
            result = appleUtils.validateAuthorizationGrantCode(client_secret, code, response);
        } else if (client_secret != null && code == null && refresh_token != null) {
            result = appleUtils.validateAnExistingRefreshToken(client_secret, refresh_token);
        }

        return result;
    }

    /**
     * Apple login page 호출을 위한 Meta 정보 가져오기
     *
     * @return
     */
    @Override
    public Map<String, String> getLoginMetaInfo() {
        return appleUtils.getMetaInfo();
    }

    /**
     * id_token에서 payload 데이터 가져오기
     *
     * @return
     */
    @Override
    public String getPayload(String id_token) {
        return appleUtils.decodeFromIdToken(id_token).toString();
    }

}
