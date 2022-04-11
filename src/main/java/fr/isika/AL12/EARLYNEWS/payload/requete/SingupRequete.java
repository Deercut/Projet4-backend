package fr.isika.AL12.EARLYNEWS.payload.requete;

import java.util.Set;
import javax.validation.constraints.*;
/**
 * @author songo
 *
 */
public class SingupRequete {

	@NotBlank
	@Size(min = 3, max= 20)
	private String username;
	
	@NotBlank
	@Size(max=80)
	@Email
	private String email;
	
	private Set<String> role;
	
	@NotBlank
	@Size(min= 6, max=50)
	private String password;

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

	public Set<String> getRole() {
		return role;
	}

	public void setRole(Set<String> role) {
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
