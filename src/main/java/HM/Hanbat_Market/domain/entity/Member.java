package HM.Hanbat_Market.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 생성자를 통해서 값 변경 목적으로 접근하는 메시지들 차단
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private AtomicLong id = new AtomicLong(); //동시성 문제해결을 위해 AtomicLong 사용

    @Column(nullable = false, unique = true)
    private String mail;

    @Column(nullable = false)
    private String passwd;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    private String nickname;

    private Member(String mail, String passwd, String phoneNumber, String nickname) {
        this.mail = mail;
        this.passwd = passwd;
        this.phoneNumber = phoneNumber;
        this.nickname = nickname;
    }

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Article> articles = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Item> items = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<PreemptionItem> preemptionItems = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<PurchaseHistory> purchaseHistories = new ArrayList<>();

    public Member createMember(String mail, String passwd, String phoneNumber, String nickname){
        return new Member(mail, passwd, phoneNumber, nickname);
    }

}
