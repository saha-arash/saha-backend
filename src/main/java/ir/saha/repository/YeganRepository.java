package ir.saha.repository;

import ir.saha.domain.Yegan;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Yegan entity.
 */
@Repository
public interface YeganRepository extends JpaRepository<Yegan, Long> {

    @Query(value = "select distinct yegan from Yegan yegan left join fetch yegan.zirYegans",
        countQuery = "select count(distinct yegan) from Yegan yegan")
    Page<Yegan> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct yegan from Yegan yegan left join fetch yegan.zirYegans")
    List<Yegan> findAllWithEagerRelationships();

    @Query("select yegan from Yegan yegan left join fetch yegan.zirYegans where yegan.id =:id")
    Optional<Yegan> findOneWithEagerRelationships(@Param("id") Long id);

}
