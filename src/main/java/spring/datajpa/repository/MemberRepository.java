package spring.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.datajpa.entity.Member;

import java.util.List;

/**
 * Spring Data JPA Repository
 * */
public interface MemberRepository extends JpaRepository<Member, Long> {//JpaRepository<T, ID>
    List<Member> findByNameAndAgeGreaterThan(String name, int age);

}
