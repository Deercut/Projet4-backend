package fr.isika.AL12.EARLYNEWS.models;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/*
 * Usermodèle dans User.java .
Il comporte 5 champs : identifiant, nom d'utilisateur, email, mot de passe, rôles.*/
/**
 * @author songo
 *
 */

@Entity
@Table(name = "utilisateur", uniqueConstraints = { 
		@UniqueConstraint(columnNames = "username"),
		@UniqueConstraint(columnNames = "email") })


public class Utilisateur {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 100)
	private String username;

	@NotBlank
	@Size(max = 100)
	@Email
	private String email;

	@NotBlank
	@Size(max = 120)
	private String password;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "utilisateur_roles", 
	
		joinColumns = @JoinColumn(name = "utilisateur_id"), 
		inverseJoinColumns = @JoinColumn(name = "roles_id"))
	private Set<Role> roles = new HashSet<>();

	public Utilisateur() {
		
	}
	
	public Utilisateur(String username,String email,String password) {
		this.username = username;
		this.email = email;
		this.password = password;
		
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	
}
