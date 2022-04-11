package fr.isika.AL12.EARLYNEWS.securite.service;
import javax.transaction.Transactional;

/**
 * @author songo
 *
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import fr.isika.AL12.EARLYNEWS.models.Utilisateur;
import fr.isika.AL12.EARLYNEWS.repository.UtilisateurRepository;


/*On a objet utilisateur entièrement personnalisé à l'aide de UserRepository,
 *  puis nous construisons un UserDetailsobjet à l'aide de la méthode statique build().*/

@Service
public class UtilisateurDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UtilisateurRepository utilisateurRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Utilisateur utilisateur = utilisateurRepository.findByUsername(username)
				.orElseThrow(()-> new UsernameNotFoundException("Utilisateur introuvable avec le nom: " + username));
		return UtilsiateurDetailsImpl.build(utilisateur);
		
/*On vas construire un UtilisateurDetails via la méthode build().*/
	}
	
	
	
}
