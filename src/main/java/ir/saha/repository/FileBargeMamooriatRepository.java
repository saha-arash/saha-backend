package ir.saha.repository;

import ir.saha.domain.FileBargeMamooriat;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the FileBargeMamooriat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FileBargeMamooriatRepository extends JpaRepository<FileBargeMamooriat, Long> {

}
