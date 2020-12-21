package ir.saha.repository;

import ir.saha.domain.Gozaresh;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Gozaresh entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GozareshRepository extends JpaRepository<Gozaresh, Long> {

}
