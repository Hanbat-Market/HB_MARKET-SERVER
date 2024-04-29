package HM.Hanbat_Market.api.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileNicknameRequest {
    String uuid;
    String nickName;
}
