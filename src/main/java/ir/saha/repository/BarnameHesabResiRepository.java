package ir.saha.repository;

import ir.saha.domain.BarnameHesabResi;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the BarnameHesabResi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BarnameHesabResiRepository extends JpaRepository<BarnameHesabResi, Long> {

}
