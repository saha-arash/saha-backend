package ir.saha.repository;

import ir.saha.domain.Mantaghe;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Mantaghe entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MantagheRepository extends JpaRepository<Mantaghe, Long> {

}
