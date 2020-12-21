package ir.saha.repository;

import ir.saha.domain.Dore;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Dore entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DoreRepository extends JpaRepository<Dore, Long> {

}
