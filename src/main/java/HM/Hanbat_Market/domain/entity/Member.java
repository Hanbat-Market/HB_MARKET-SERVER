package HM.Hanbat_Market.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 생성자를 통해서 값 변경 목적으로 접근하는 메시지들 차단
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id; //동시성 문제해결을 위해 추후에 AtomicLong 사용

    @Column(nullable = false, unique = true)
    private String mail;

    @Column(nullable = false)
    private String passwd;

    @Column(nullable = false, unique = true)
    private String nickname;

    @OneToMany(mappedBy = "member")
    private List<Article> articles = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Item> items = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<PreemptionItem> preemptionItems = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Trade> trades = new ArrayList<>();

    private void setMail(String mail){
        this.mail = mail;
    }

    private Member(String mail, String passwd, String nickname) {
        this.mail = mail;
        this.passwd = passwd;
        this.nickname = nickname;
    }

    public static Member createMember(String mail, String passwd, String nickname) {
        return new Member(mail, passwd, nickname);
    }

    public void changeMail(String mail) {
        setMail(mail);
    }

}
