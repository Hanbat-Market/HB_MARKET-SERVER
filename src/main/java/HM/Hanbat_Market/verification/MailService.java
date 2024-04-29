package HM.Hanbat_Market.verification;

import HM.Hanbat_Market.domain.entity.Member;
import HM.Hanbat_Market.repository.member.MemberRepository;
import HM.Hanbat_Market.service.APIURL;
import HM.Hanbat_Market.service.member.MemberService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MailService {

    private final JavaMailSender javaMailSender;
    private static final String senderEmail = APIURL.senderEmail;
    private static int number;

    public static void createNumber() {
        number = (int) (Math.random() * (90000)) + 100000;// (int) Math.random() * (최댓값-최소값+1) + 최소값
    }

    public MimeMessage CreateMail(String mail) {
        createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(mail));
            message.setFrom(senderEmail);
            message.setSubject("[한밭마켓] 재학생 인증을 위한 인증번호 안내");

            String body = "";
            body += "<div style='font-family: Arial, sans-serif;'>";
            body += "<div style='border-top: 2px solid #60b9ce; border-right: 1px solid #e7e7e7; border-left: 1px solid #e7e7e7; border-bottom: 1px solid #e7e7e7; width: 670px; margin: 0 auto;'>";
            body += "<div style='background-color: #ffffff; padding: 40px 30px 0 35px; text-align: center;'>";
            body += "<h3 style='color: #2daad1; font-size: 25px; text-align: left; width: 352px; word-spacing: -1px; vertical-align: top;'>인증 번호 확인 후<br>이메일 인증을 완료해 주세요.</h3>";
            body += "</div>";
            body += "<div style='text-align: center;'>";
            body += "<table style='text-align: left; font-family: Arial, sans-serif; width: 605px; margin: 0 auto;'>";
            body += "<tr><td style='color: #555; font-size: 24px; vertical-align: bottom; height: 27px;'>안녕하세요? 한밭마켓입니다.</td></tr>";
            body += "<tr><td style='font-size: 22px; word-spacing: -1px; height: 30px;'>아래 인증번호를 입력하시고 재학생 인증을 완료해주세요.</td></tr>";
            body += "</table>";
            body += "<div style='background-color: #3cbfaf; font-family: Arial, sans-serif; width: 341px; margin: 0 auto;'>";
            body += "<table style='text-align: center; color: #fff; width: 100%;'>";
            body += "<tr><td height='98' style='font-size: 41px;'><span>" + number + "</span></td></tr>";
            body += "</table>";
            body += "</div>";
            body += "</div>";
            body += "</div>";
            body += "</div>";
            message.setText(body, "UTF-8", "html");

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;
    }

    public int sendMail(String mail) {
        MimeMessage message = CreateMail(mail);
        javaMailSender.send(message);

        return number;
    }
}