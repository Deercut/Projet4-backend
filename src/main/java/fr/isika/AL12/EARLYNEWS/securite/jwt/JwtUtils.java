package fr.isika.AL12.EARLYNEWS.securite.jwt;
/**
 * @author songo
 *
 */
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import fr.isika.AL12.EARLYNEWS.securite.service.UtilsiateurDetailsImpl;
import io.jsonwebtoken.*;


/*Cette classe a 3 fonctions :

générer un JWTnom d'utilisateur, date, expiration, secret
obtenir le nom d'utilisateur deJWT
valider JWT*/


@Component
public class JwtUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
	
	@Value("${earlynews.app.jwtSecret}")
	private String jwtSecret;
	
	@Value("${earlynews.app.jwtExpiration}")
	private int jwtExpirationMs;
	
	public String genererJwtToken(Authentication authentication) {
		
		UtilsiateurDetailsImpl utilisateurPrincipal = (UtilsiateurDetailsImpl)authentication.getPrincipal();
		
		return Jwts.builder()
			.setSubject((utilisateurPrincipal.getUsername()))
			.setIssuedAt(new Date())
			.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
			.signWith(SignatureAlgorithm.HS512, jwtSecret)
			.compact();
		
	}
	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}
	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		}catch (SignatureException e){
			logger.error("Signature JWT INVALIDE: {}", e.getMessage());
		}catch(MalformedJwtException e) {
			logger.error("JWT TOKEN INVALIDE: {}", e.getMessage());
		}catch(ExpiredJwtException e) {
			logger.error(" JWT TOKEN EST EXPIRE: {}", e.getMessage());
		}catch(UnsupportedJwtException e) {
			logger.error("JWT TOKEN EST PAS SUPPORTE: {}", e.getMessage());
		}catch(IllegalArgumentException e) {
			logger.error(" LA DEMANDE JWT STRING EST VIDE: {}", e.getMessage());
		}
		return false;
	}

}
