package ir.saha.repository;

import ir.saha.domain.Payam;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Payam entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PayamRepository extends JpaRepository<Payam, Long> {

}
