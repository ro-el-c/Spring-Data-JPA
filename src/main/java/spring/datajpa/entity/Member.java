package spring.datajpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //기본 생성자의 access level을 protected로 설정
// JPA에서 Entity는 기본 생성자가 필요하며, private으로 설정하면 안 됨. protected까지는 열어둬야 함
public class Member {
    @Id @GeneratedValue
    private Long id;
    private String name;

    public Member(String name) {
        this.name = name;
    }
}
