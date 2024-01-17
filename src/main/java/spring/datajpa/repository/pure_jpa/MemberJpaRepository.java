package spring.datajpa.repository.pure_jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import spring.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

/**
 * 순수 JPA 기반 Repository
 * */
@Repository
public class MemberJpaRepository {
    @PersistenceContext
    private EntityManager em;

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public void delete(Member member) {
        em.remove(member);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public long count() {
        return em.createQuery("select count(m) from Member m", Long.class)
                .getSingleResult();
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findByNameAndAgeGreaterThan(String name, int age) {
        return em.createQuery("select m from Member m where m.name=:name and m.age>:age", Member.class)
                .setParameter("name", name)
                .setParameter("age", age)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createNamedQuery("Member.findByName", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

}
