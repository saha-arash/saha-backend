package ir.saha.repository;

import ir.saha.domain.YeganCode;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the YeganCode entity.
 */
@SuppressWarnings("unused")
@Repository
public interface YeganCodeRepository extends JpaRepository<YeganCode, Long> {

}
