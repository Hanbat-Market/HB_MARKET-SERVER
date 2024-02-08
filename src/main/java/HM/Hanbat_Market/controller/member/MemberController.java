package HM.Hanbat_Market.controller.member;

import HM.Hanbat_Market.controller.member.dto.MemberForm;
import HM.Hanbat_Market.controller.member.login.SessionConst;
import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.service.member.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model
            , @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member sessionMember) {
        if (sessionMember != null) {
            return "redirect:/";
        }
        model.addAttribute("memberForm", new MemberForm()); // = @ModelAttribute
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid @ModelAttribute MemberForm form, BindingResult result
            , @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member sessionMember) {
        if (sessionMember != null) {
            return "redirect:/";
        }

        if (result.hasErrors()) {
            return "members/createMemberForm";
        }
        Member member = Member.createMember(form.getMail(), form.getPasswd(), form.getMail(), form.getNickname());
        memberService.join(member);
        return "members/completeRegister";
    }
}
