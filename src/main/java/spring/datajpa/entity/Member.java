package spring.datajpa.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //기본 생성자의 access level을 protected로 설정
// JPA에서 Entity는 기본 생성자가 필요하며, private으로 설정하면 안 됨. protected까지는 열어둬야 함
@ToString(of = {"id", "name", "age"})
@NamedQuery(
        name = "Member.findByName",
        query = "select m from Member m where m.name=:name"
)
@NamedEntityGraph(
        name = "Member.all",
        attributeNodes = @NamedAttributeNode("team")
)
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String name;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String name) {
        this.name = name;
    }

    public Member(String name, int age, Team team) {
        this.name = name;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }
    }

    public Member(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void changeTeam(Team team) {
        this.team = team;
        team.addMember(this);
    }
}
