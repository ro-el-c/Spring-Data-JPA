package spring.datajpa.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import spring.datajpa.dto.MemberDto;
import spring.datajpa.entity.Member;
import spring.datajpa.entity.Team;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
//@Rollback(value = false)
class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();
        /*
        * 이렇게 바로 .get() 하는 것은 좋은 방법 X
        * orElse() 등의 방식으로 값이 존재하지 않는 경우에 대한 방향을 제시하는 것이 좋음
        * */

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getName()).isEqualTo(member.getName());
        assertThat(findMember).isEqualTo(member); // findMember == member
    }

    @Test
    @DisplayName("Spring Data JPA 공통 인터페이스 사용 CRUD 테스트")
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //전체 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        count = memberRepository.count();
        assertThat(count).isEqualTo(0);

    }

    @Test
    public void findByNameAndAgeGreaterThen() {
        Member m1 = new Member("AAA", 20);
        Member m2 = new Member("AAA", 22);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNameAndAgeGreaterThan("AAA", 21);

        assertThat(result.get(0).getName()).isEqualTo(m2.getName());
        assertThat(result.get(0).getAge()).isEqualTo(m2.getAge());
        assertThat(result.size()).isEqualTo(1);

    }

    @Test
    public void testNamedQuery() {
        //given
        Member m1 = new Member("AAA", 20);
        Member m2 = new Member("AAA", 22);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //when
        List<Member> result = memberRepository.findByName("AAA");

        //then
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(m1);
    }

    @Test
    public void testQuery() {
        //given
        Member m1 = new Member("AAA", 20);
        Member m2 = new Member("AAA", 22);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //when
        List<Member> result = memberRepository.findUser("AAA", 20);

        //then
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(m1);
    }

    @Test
    public void findNameList() {
        //given
        Member m1 = new Member("AAA", 20);
        Member m2 = new Member("BBB", 22);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //when
        List<String> result = memberRepository.findNameList();

        //then
        assertThat(result.size()).isEqualTo(2);
        for (String str : result) {
            System.out.println("str = " + str);
        }
    }

    @Test
    public void findMemberDto() {
        //given
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA", 20);
        m1.setTeam(team);
        memberRepository.save(m1);

        //when
        List<MemberDto> result = memberRepository.findMemberDto();

        //then
        for (MemberDto dto : result) {
            System.out.println("dto = " + dto);
        }
    }

    @Test
    public void findByNames() {
        //given
        Member m1 = new Member("AAA", 20);
        Member m2 = new Member("BBB", 22);
        Member m3 = new Member("CCC", 25);
        memberRepository.save(m1);
        memberRepository.save(m2);
        memberRepository.save(m3);

        //when
        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "CCC"));

        //then
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void paging() {
        //given
        memberRepository.save(new Member("member1", 25));
        memberRepository.save(new Member("member2", 25));
        memberRepository.save(new Member("member3", 25));
        memberRepository.save(new Member("member4", 25));
        memberRepository.save(new Member("member5", 25));
        memberRepository.save(new Member("member6", 25));
        memberRepository.save(new Member("member7", 25));

        //when
        int age=25;
        // 0페이지에서 3개의 결과, 이름에 대하여 내림차순
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "name"));

        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        //then
        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();// total count

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(7);
        assertThat(page.getNumber()).isEqualTo(0); //page.getNumber(): 페이지 번호
        assertThat(page.getTotalPages()).isEqualTo(3); //7개의 데이터를 3개씩 가져옴 -> 3페이지
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

}