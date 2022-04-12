package fr.isika.AL12.EARLYNEWS.repository;

/*Rôle Repository

Ce référentiel étend également JpaRepositoryet fournit une méthode de recherche.*/
/**
 * @author songo
 *
 */
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.isika.AL12.EARLYNEWS.models.ERole;
import fr.isika.AL12.EARLYNEWS.models.Role;
/**
 * @author songo
 *
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	
	Optional<Role> findByName(ERole name);
}
