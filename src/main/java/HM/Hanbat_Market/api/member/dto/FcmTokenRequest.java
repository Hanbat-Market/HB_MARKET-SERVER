package HM.Hanbat_Market.api.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
public class FcmTokenRequest {
    private String targetMemberUuid;
    private String fcmToken;
}
