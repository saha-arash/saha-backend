package ir.saha.repository;

import ir.saha.domain.Ostan;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Ostan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OstanRepository extends JpaRepository<Ostan, Long> {

}
