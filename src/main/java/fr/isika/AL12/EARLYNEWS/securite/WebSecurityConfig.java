package fr.isika.AL12.EARLYNEWS.securite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import fr.isika.AL12.EARLYNEWS.securite.jwt.AuthEntryPointJwt;
import fr.isika.AL12.EARLYNEWS.securite.jwt.AuthTokenFilter;
import fr.isika.AL12.EARLYNEWS.securite.service.UserDetailsServiceImpl;

/* PasswordEncoderpour le DaoAuthenticationProvider. Sinon, il utilisera du texte brut.*/

/*WebSecurityConfigclasse qui étend 
 * WebSecurityConfigurerAdapter.*/
/**
 * @author songo
 *
 */

/*
 * @EnableGlobalMethodSecurity
 * assure la sécurité AOP sur les méthodes. Il permet @PreAuthorize, @PostAuthorize, il prend également en charge JSR-250 */
/*@EnableWebSecurity permet à Spring de trouver et d'appliquer automatiquement la classe à la sécurité Web globale.*/

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		// securedEnabled = true,
		// jsr250Enabled = true,
		prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	//Aide à gérer les problèmes de cors et csrf((Cross-Site Request Forgery))
	//crsf = type de vulnérabilité des services d'authentification web.
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable()
			.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.authorizeRequests().antMatchers("/api/auth/**").permitAll()
			.antMatchers("/api/test/**").permitAll()
			.antMatchers(HttpMethod.OPTIONS, "/**")
			.permitAll()
			.anyRequest().authenticated().and().httpBasic();

		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	
	/*la méthode configure(HttpSecurity http) de l' WebSecurityConfigurerAdapterinterface. 
	 * Indique à Spring Security comment nous configurons CORS et CSRF, 
	 * quand nous voulons exiger que tous les utilisateurs soient authentifiés ou non, 
	 * quel filtre ( AuthTokenFilter) et quand nous voulons que cela fonctionne 
	 * (filtre avant UsernamePasswordAuthenticationFilter), quel gestionnaire d'exception est choisi ( AuthEntryPointJwt).*/
	
	
}















