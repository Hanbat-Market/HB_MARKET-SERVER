package HM.Hanbat_Market.controller.member.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberForm{
    @NotEmpty(message = "메일을 입력해주세요.")
    String mail;

    @NotEmpty(message = "비밀번호를 입력해주세요.")
    String passwd;

    @NotEmpty(message = "전화번호를 입력해주세요.")
    String phoneNumber;

    @NotEmpty(message = "닉네임을 입력해주세요.")
    String nickname;
}
