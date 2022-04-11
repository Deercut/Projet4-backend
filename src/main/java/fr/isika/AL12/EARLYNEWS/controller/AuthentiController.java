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

import fr.isika.AL12.EARLYNEWS.models.EnumRole;
import fr.isika.AL12.EARLYNEWS.models.Role;
import fr.isika.AL12.EARLYNEWS.models.Utilisateur;
import fr.isika.AL12.EARLYNEWS.payload.reponse.JwtReponse;
import fr.isika.AL12.EARLYNEWS.payload.reponse.MessageReponse;
import fr.isika.AL12.EARLYNEWS.payload.requete.LoginRequest;
import fr.isika.AL12.EARLYNEWS.payload.requete.SingupRequete;
import fr.isika.AL12.EARLYNEWS.repository.RoleRepository;
import fr.isika.AL12.EARLYNEWS.repository.UtilisateurRepository;
import fr.isika.AL12.EARLYNEWS.securite.jwt.JwtUtils;
import fr.isika.AL12.EARLYNEWS.securite.service.UtilsiateurDetailsImpl;

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
	UtilisateurRepository utilisateurRepository;
	
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
		String jwt = jwtUtils.genererJwtToken(authentication);
		
		UtilsiateurDetailsImpl utilisateurDetails = (UtilsiateurDetailsImpl) authentication.getPrincipal();	
		List<String> roles = utilisateurDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		
		//A re vérifier le retoure par rapport à JwtReponse
		return ResponseEntity.ok(new JwtReponse(jwt,
				utilisateurDetails.getId(),
				utilisateurDetails.getUsername(),
				utilisateurDetails.getEmail(),
				roles));
		
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SingupRequete singUpRequete ){
		if( utilisateurRepository.existsByUsername(singUpRequete.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageReponse("Erreur : nom d'utilisateur déjà éxistant"));
		}
		
		if (utilisateurRepository.existsByEmail(singUpRequete.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageReponse("Erreur : email déjà éxistant "));
		}
		
		
		//Creer un nouveau compte utilisateur
		Utilisateur utilisateur = new Utilisateur(singUpRequete.getUsername(),
												   singUpRequete.getEmail(),
												   encoder.encode(singUpRequete.getPassword()));
		
		Set<String> strRoles = singUpRequete.getRole();
		Set<Role> roles = new HashSet<>();
		if(strRoles == null) {
			Role userRole = roleRepository.findByName(EnumRole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Erreur : role introuvable."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role ->{
				switch(role) {
				case "admin":
					Role adminRole = roleRepository.findByName(EnumRole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Erreur : role introuvable"));
					roles.add(adminRole);
					
					break;
				default: 
					Role userRole = roleRepository.findByName(EnumRole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Erreur : role introuvable"));
					roles.add(userRole);
				}
			});
		}
		
	utilisateur.setRoles(roles);
		utilisateurRepository.save(utilisateur);
		return ResponseEntity.ok(new MessageReponse("Enregistrement utilisateur succes !"));
		
		
	}
	
	
	
}
