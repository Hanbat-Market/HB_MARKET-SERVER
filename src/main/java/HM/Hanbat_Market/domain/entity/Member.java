package HM.Hanbat_Market.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 생성자를 통해서 값 변경 목적으로 접근하는 메시지들 차단
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id; //동시성 문제해결을 위해 추후에 AtomicLong 사용

    private String uuid;

    @Column(nullable = false, unique = true)
    private String mail;

    @Column(nullable = false)
    private String passwd;

    @Column(nullable = false, unique = true)
    private String nickname;

    private String name;

    @Enumerated(EnumType.STRING) // Enum 타입은 문자열 형태로 저장해야 함
    private Role role;

    @OneToMany(mappedBy = "member")
    private List<Article> articles = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Item> items = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<PreemptionItem> preemptionItems = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Trade> trades = new ArrayList<>();

    private void setMail(String mail) {
        this.mail = mail;
    }

    private Member(String mail, String passwd, String nickname) {
        this.mail = mail;
        this.passwd = passwd;
        this.nickname = nickname;
    }

    private Member(String mail, String name, String passwd, String nickname, Role role) {
        this.uuid = UUID.randomUUID().toString();
        this.mail = mail;
        this.name = name;
        this.passwd = passwd;
        this.nickname = nickname;
        this.role = role;
    }

    public static Member createMember(String mail, String passwd, String nickname) {
        Member member = new Member(mail, passwd, nickname);
        member.uuid = "test" + passwd;
        return member;
    }

    public static Member createMember(String mail, String name, String passwd, String nickname, Role role) {
        return new Member(mail, name, passwd, nickname, role);
    }

    public Member update(String name) {
        this.name = name;

        return this;
    }
    public void changeMail(String mail) {
        setMail(mail);
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return Role.USER.getKey();
            }
        });

        return collection;
    }
}
