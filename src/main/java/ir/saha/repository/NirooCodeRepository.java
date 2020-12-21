package ir.saha.repository;

import ir.saha.domain.NirooCode;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the NirooCode entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NirooCodeRepository extends JpaRepository<NirooCode, Long> {

}
