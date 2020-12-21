package ir.saha.repository;

import ir.saha.domain.FileGozaresh;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the FileGozaresh entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FileGozareshRepository extends JpaRepository<FileGozaresh, Long> {

}
