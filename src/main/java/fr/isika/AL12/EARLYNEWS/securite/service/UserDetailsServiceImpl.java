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

import fr.isika.AL12.EARLYNEWS.models.User;
import fr.isika.AL12.EARLYNEWS.repository.UserRepository;



/*On a objet utilisateur entièrement personnalisé à l'aide de UserRepository,
 *  puis nous construisons un UserDetailsobjet à l'aide de la méthode statique build().*/

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Introuvable à ce nom: " + username));

		return UserDetailsImpl.build(user);
		
/*On vas construire un UtilisateurDetails via la méthode build().*/
	}
	
	
	
}
