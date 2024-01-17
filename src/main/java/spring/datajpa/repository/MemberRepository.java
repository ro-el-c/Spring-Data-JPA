package spring.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import spring.datajpa.dto.MemberDto;
import spring.datajpa.entity.Member;

import java.util.List;

/**
 * Spring Data JPA Repository
 * */
public interface MemberRepository extends JpaRepository<Member, Long> {//JpaRepository<T, ID>
    List<Member> findByNameAndAgeGreaterThan(String name, int age);

//    @Query(name = "Member.findByName") // 없어도 잘 동작함
    List<Member> findByName(@Param("name") String name);

    @Query("select m from Member m where m.name=:name and m.age=:age")
    List<Member> findUser(@Param("name") String name, @Param("age") int age);

    @Query("select new spring.datajpa.dto.MemberDto(m.id, m.name, t.name) " +
            "from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m.name from Member m")
    List<String> findNameList();
}
