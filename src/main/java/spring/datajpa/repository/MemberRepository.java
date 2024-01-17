package spring.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import spring.datajpa.dto.MemberDto;
import spring.datajpa.entity.Member;

import java.util.Collection;
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

    @Query("select m from Member m where m.name in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    /*
     - 검색 조건: 나이가 10살
     - 정렬 조건: 이름으로 내림차순
     - 페이징 조건: 첫 번째 페이지, 페이지당 보여줄 데이터는 3건
    * */
    Page<Member> findByAge(int age, Pageable pageable);
    Slice<Member> findSliceByAge(int age, Pageable pageable);
}
