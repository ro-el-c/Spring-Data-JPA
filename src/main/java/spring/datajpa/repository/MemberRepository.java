package spring.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import spring.datajpa.entity.Member;

import java.util.List;

/**
 * Spring Data JPA Repository
 * */
public interface MemberRepository extends JpaRepository<Member, Long> {//JpaRepository<T, ID>
    List<Member> findByNameAndAgeGreaterThan(String name, int age);

//    @Query(name = "Member.findByName") // 없어도 잘 동작함
    List<Member> findByName(@Param("name") String name);

}
