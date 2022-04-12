/**
 * 
 */
package fr.isika.AL12.EARLYNEWS.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.isika.AL12.EARLYNEWS.models.ERole;
import fr.isika.AL12.EARLYNEWS.models.Role;
import fr.isika.AL12.EARLYNEWS.models.User;
import fr.isika.AL12.EARLYNEWS.payload.reponse.JwtResponse;
import fr.isika.AL12.EARLYNEWS.payload.reponse.MessageReponse;
import fr.isika.AL12.EARLYNEWS.payload.requete.LoginRequest;
import fr.isika.AL12.EARLYNEWS.payload.requete.SignupRequest;
import fr.isika.AL12.EARLYNEWS.repository.RoleRepository;
import fr.isika.AL12.EARLYNEWS.repository.UserRepository;
import fr.isika.AL12.EARLYNEWS.securite.jwt.JwtUtils;
import fr.isika.AL12.EARLYNEWS.securite.service.UserDetailsImpl;

/**
 * @author songo
 *
 */

/*les contrôleurs gèrent les demandes d'inscription/de 
 * connexion et les demandes autorisées.*/

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthentiController {
	
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	PasswordEncoder encoder;
	
	@Autowired
	JwtUtils jwtUtils;
	
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
		
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();	
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		
		//A re vérifier le retoure par rapport à JwtReponse
		return ResponseEntity.ok(new JwtResponse(jwt,
				userDetails.getId(),
				userDetails.getUsername(),
				userDetails.getEmail(),
				roles));
		
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest singUpRequete ){
		if( userRepository.existsByUsername(singUpRequete.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageReponse("Erreur : nom d'utilisateur déjà éxistant"));
		}
		
		if (userRepository.existsByEmail(singUpRequete.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageReponse("Erreur : email déjà éxistant "));
		}
		
		
		//Creer un nouveau compte utilisateur
		User utilisateur = new User(singUpRequete.getUsername(),
												   singUpRequete.getEmail(),
												   encoder.encode(singUpRequete.getPassword()));
		
		Set<String> strRoles = singUpRequete.getRole();
		Set<Role> roles = new HashSet<>();
		if(strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Erreur : role introuvable."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role ->{
				switch(role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Erreur : role introuvable"));
					roles.add(adminRole);
					
					break;
				default: 
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Erreur : role introuvable"));
					roles.add(userRole);
				}
			});
		}
		
	utilisateur.setRoles(roles);
		userRepository.save(utilisateur);
		return ResponseEntity.ok(new MessageReponse("Enregistrement utilisateur succes !"));
		
		
	}
	
	
	
}
