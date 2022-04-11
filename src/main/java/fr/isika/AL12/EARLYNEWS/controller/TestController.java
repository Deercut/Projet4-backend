package fr.isika.AL12.EARLYNEWS.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*les contrôleurs gèrent les demandes d'inscription/de 
 * connexion et les demandes autorisées.*/

@CrossOrigin(origins= "*",maxAge= 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
	
	@GetMapping("/all")
	public String allAccess() {
		return "En cours de construction...";
	}
	
	@GetMapping("/user")
	@PreAuthorize("hasRole('USER')")
	public String utilisateurAccess() {
		return "Utilisateur Contenu";
	}
	
	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return "Ici c'est juste pour tester.";
	}

}
