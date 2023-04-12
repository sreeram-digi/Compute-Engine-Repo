package com.portal.security;

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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.portal.ApplicationConstants;
import com.portal.filter.JwtRequestFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private UserDetailsService jwtUserDetailsService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(jwtUserDetailsService);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		// We don't need CSRF for this example
		httpSecurity.csrf().disable();
		
		// dont authenticate this particular request
		httpSecurity
				.authorizeRequests()
				.antMatchers("/login","/loginWithToken").permitAll()
				.antMatchers("/actuator/**").permitAll()
				.antMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
				.antMatchers(HttpMethod.GET, "/interviewer/admins").permitAll()
				.antMatchers("/interviewer", "/interviewer/^/(?!admins)([A-Za-z0-9-]+)$","/criteria**","/criteriaGroup**").hasAuthority(ApplicationConstants.AUTHORITY_ADMIN)
				.antMatchers("/workflow","/workflow/*").hasAnyAuthority(ApplicationConstants.AUTHORITY_ADMIN,ApplicationConstants.AUTHORITY_SELECTOR, ApplicationConstants.AUTHORITY_HR,ApplicationConstants.AUTHORITY_INTERVIEWER)
				//Rule to view candidate not allowed for external user eg:/candidate/id
				.antMatchers(HttpMethod.GET,"/candidate/[A-Za-z0-9-]+$").hasAnyAuthority(ApplicationConstants.AUTHORITY_ADMIN,ApplicationConstants.AUTHORITY_SELECTOR, ApplicationConstants.AUTHORITY_HR,ApplicationConstants.AUTHORITY_INTERVIEWER)
				// all other requests need to be authenticated
				.anyRequest().authenticated()
				.and().
				// make sure we use stateless session; session won't be used to
				// store user's state.
				exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		// Add a filter to validate the tokens with every request
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
}
