package ir.saha.repository;

import ir.saha.domain.FileName;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the FileName entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FileNameRepository extends JpaRepository<FileName, Long> {

}
