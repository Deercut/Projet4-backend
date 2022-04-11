package fr.isika.AL12.EARLYNEWS.securite.jwt;
/**
 * @author songo
 *
 */
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class AuthentiPointEntreeJwt implements AuthenticationEntryPoint {

	private static final Logger logger = LoggerFactory.getLogger(AuthentiPointEntreeJwt.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		logger.error("Unauthorized error: {}", authException.getMessage());
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
		
	}
	
	
	/*AuthEntryPointJwtclasse qui implémente l' AuthenticationEntryPointinterface
	 * Commence(), méthode sera déclenchée chaque fois qu'un utilisateur non authentifié demandera une 
	 *  ressource HTTP sécurisée et qu'un an AuthenticationExceptionsera lancé.*/
	//Juste un petit test
	
}