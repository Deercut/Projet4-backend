package fr.isika.AL12.EARLYNEWS.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.isika.AL12.EARLYNEWS.models.Utilisateur;

/*Référentiel utilisateur

Il existe 3 méthodes nécessaires qui JpaRepositoryprennent en charge*/
/**
 * @author songo
 *
 */
@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
	Optional<Utilisateur> findByUsername(String username);
	
	Boolean existsByUsername(String username);
	
	Boolean existsByEmail(String email);
	
	
}
