package HM.Hanbat_Market.service.account.apple.apple.controller;

import HM.Hanbat_Market.api.Result;
import HM.Hanbat_Market.service.account.apple.apple.model.AppsResponse;
import HM.Hanbat_Market.service.account.apple.apple.model.ServicesResponse;
import HM.Hanbat_Market.service.account.apple.apple.model.TokenResponse;
import HM.Hanbat_Market.service.account.apple.apple.service.AppleService;

import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AppleController {

    private Logger logger = LoggerFactory.getLogger(AppleController.class);

    private final AppleService appleService;

    /**
     * Sign in with Apple - JS Page (index.html)
     *
     * @param model
     * @return
     */
    @GetMapping(value = "/apple")
    public String appleLoginPage(ModelMap model) {

        Map<String, String> metaInfo = appleService.getLoginMetaInfo();

        model.addAttribute("client_id", metaInfo.get("CLIENT_ID"));
        model.addAttribute("redirect_uri", metaInfo.get("REDIRECT_URI"));
        model.addAttribute("nonce", metaInfo.get("NONCE"));

        return "index";
    }

    /**
     * Apple login page Controller (SSL - https)
     *
     * @param model
     * @return
     */
    @GetMapping(value = "/apple/login")
    public String appleLogin(ModelMap model) {

        Map<String, String> metaInfo = appleService.getLoginMetaInfo();

        model.addAttribute("client_id", metaInfo.get("CLIENT_ID"));
        model.addAttribute("redirect_uri", metaInfo.get("REDIRECT_URI"));
        model.addAttribute("nonce", metaInfo.get("NONCE"));
        model.addAttribute("response_type", "code id_token");
        model.addAttribute("scope", "name email");
        model.addAttribute("response_mode", "form_post");

        return "redirect:https://appleid.apple.com/auth/authorize";
    }

    /**
     * Apple Login 유저 정보를 받은 후 권한 생성
     *
     * @param serviceResponse
     * @return
     */
    @PostMapping(value = "/redirect")
    @ResponseBody
    public Result servicesRedirect(ServicesResponse serviceResponse, HttpServletResponse response)
            throws NoSuchAlgorithmException, IOException, ParseException, JOSEException {

        if (serviceResponse == null) {
            return null;
        }

        String code = serviceResponse.getCode();
        String client_secret = appleService.getAppleClientSecret(serviceResponse.getId_token());

        logger.debug("================================");
        logger.debug("id_token ‣ " + serviceResponse.getId_token());
        logger.debug("payload ‣ " + appleService.getPayload(serviceResponse.getId_token()));
        logger.debug("client_secret ‣ " + client_secret);
        logger.debug("================================");

        return appleService.requestCodeValidations(client_secret, code, null, response);
    }

    /**
     * refresh_token 유효성 검사
     *
     * @param client_secret
     * @param refresh_token
     * @return
     */
    @PostMapping(value = "/refresh")
    @ResponseBody
    public Result refreshRedirect(@RequestParam String client_secret, @RequestParam String refresh_token, HttpServletResponse response)
            throws IOException, ParseException, JOSEException {
        return appleService.requestCodeValidations(client_secret, null, refresh_token, response);
    }

    /**
     * Apple 유저의 이메일 변경, 서비스 해지, 계정 탈퇴에 대한 Notifications을 받는 Controller (SSL - https (default: 443))
     *
     * @param appsResponse
     */
    @PostMapping(value = "/apps/to/endpoint")
    @ResponseBody
    public void appsToEndpoint(@RequestBody AppsResponse appsResponse) {
        logger.debug("[/path/to/endpoint] RequestBody ‣ " + appsResponse.getPayload());
    }

}
