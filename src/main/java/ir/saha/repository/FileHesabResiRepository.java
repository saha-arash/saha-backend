package ir.saha.repository;

import ir.saha.domain.FileHesabResi;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the FileHesabResi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FileHesabResiRepository extends JpaRepository<FileHesabResi, Long> {

}
