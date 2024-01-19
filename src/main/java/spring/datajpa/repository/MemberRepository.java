package spring.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Query("select m from Member m where m.name=:name and m.age = :age")
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
    @Query(value = "select m from Member m left join m.team t"
            , countQuery = "select count(m) from Member m") // count query 분리
    Page<Member> findByAge(int age, Pageable pageable);
    Slice<Member> findSliceByAge(int age, Pageable pageable);

    //벌크성 수정 쿼리
    @Modifying(clearAutomatically = true) //필수
    @Query("update Member m set m.age = m.age + 1 " +
            "where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    //@EntityGraph(attributePaths = {"fetch join 할 객체의 필드명"})
    //1. findAll()
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();
    //2. @Query
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findByQueryAndEntityGraph();
    //3. 메서드로 쿼리 생성
    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByName(@Param("name") String name);

    //@NamedEntityGraph(설정한 NamedEntityGraph name)
    @EntityGraph("Member.all")
    List<Member> findNamedEntityGraphByName(@Param("name") String name);


}
