package spring.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import spring.datajpa.entity.Member;
import spring.datajpa.repository.pure_jpa.MemberJpaRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberJpaRepositoryTest {
    @Autowired MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.find(member.getId());

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getName()).isEqualTo(member.getName());
        assertThat(findMember).isEqualTo(member); // findMember == member
        // 같은 트랜잭션 안에서 동일성 보장 - 1차 캐시
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        //단건 조회 검증
        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //전체 조회 검증
        List<Member> all = memberJpaRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);
        count = memberJpaRepository.count();
        assertThat(count).isEqualTo(0);

    }

    @Test
    public void findByNameAndAgeGreaterThen() {
        Member m1 = new Member("AAA", 20);
        Member m2 = new Member("AAA", 22);
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        List<Member> result = memberJpaRepository.findByNameAndAgeGreaterThan("AAA", 21);

        assertThat(result.get(0).getName()).isEqualTo(m2.getName());
        assertThat(result.get(0).getAge()).isEqualTo(m2.getAge());
        assertThat(result.size()).isEqualTo(1);

    }

    @Test
    public void testNamedQuery() {
        //given
        Member m1 = new Member("AAA", 20);
        Member m2 = new Member("AAA", 22);
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        //when
        List<Member> result = memberJpaRepository.findByName("AAA");

        //then
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(m1);
    }

    @Test
    public void paging() {
        //given
        memberJpaRepository.save(new Member("member1", 25));
        memberJpaRepository.save(new Member("member2", 25));
        memberJpaRepository.save(new Member("member3", 25));
        memberJpaRepository.save(new Member("member4", 25));
        memberJpaRepository.save(new Member("member5", 25));
        memberJpaRepository.save(new Member("member6", 25));
        memberJpaRepository.save(new Member("member7", 25));

        //offset, limit 계산은 직접
        int age=25;
        int offset=0;
        int limit=3;

        //when
        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);

        // 페이지 계산 공식 적용 / 페이징 객체 사용 등
        // totalPage = totalCount / size ...
        // 마지막 페이지 ...
        // 최초 페이지 ..

        //then
        assertThat(members.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(7);
    }
}