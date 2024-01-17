package spring.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.datajpa.entity.Team;

import java.util.List;

/**
 * Spring Data JPA Repository
 * */
public interface TeamRepository extends JpaRepository<Team, Long> {//JpaRepository<T, ID>
}
