package ir.saha.repository;

import ir.saha.domain.Negahbani;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Negahbani entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NegahbaniRepository extends JpaRepository<Negahbani, Long> {

}
