package ir.saha.repository;

import ir.saha.domain.HesabResi;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the HesabResi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HesabResiRepository extends JpaRepository<HesabResi, Long> {

}
