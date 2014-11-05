package fr.univrouen.telephonie.repository;

import fr.univrouen.telephonie.domain.Telephone;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Telephone entity.
 */
public interface TelephoneRepository extends JpaRepository<Telephone, Long> {

}
