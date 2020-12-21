package ir.saha.repository;

import ir.saha.domain.Karbar;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Karbar entity.
 */
@Repository
public interface KarbarRepository extends JpaRepository<Karbar, Long> {

    @Query(value = "select distinct karbar from Karbar karbar left join fetch karbar.bargeMamoorits left join fetch karbar.binanadeBargeMamoorits",
        countQuery = "select count(distinct karbar) from Karbar karbar")
    Page<Karbar> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct karbar from Karbar karbar left join fetch karbar.bargeMamoorits left join fetch karbar.binanadeBargeMamoorits")
    List<Karbar> findAllWithEagerRelationships();

    @Query("select karbar from Karbar karbar left join fetch karbar.bargeMamoorits left join fetch karbar.binanadeBargeMamoorits where karbar.id =:id")
    Optional<Karbar> findOneWithEagerRelationships(@Param("id") Long id);

}
