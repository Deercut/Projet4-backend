package fr.isika.AL12.EARLYNEWS.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.isika.AL12.EARLYNEWS.models.User;

/*Référentiel utilisateur

Il existe 3 méthodes nécessaires qui JpaRepositoryprennent en charge*/
/**
 * @author songo
 *
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);
	
	List<User> findAll();
	
	void deleteById(long id);
	
}
