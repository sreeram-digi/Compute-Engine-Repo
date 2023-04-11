package com.portal.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.portal.ApplicationConstants;
import com.portal.service.JwtUserDetailsService;
import com.portal.utils.JwtTokenUtil;

@Component
@Order(2)
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		final String requestTokenHeader = request.getHeader("token");

		String username = null;
		String jwtToken = null;
		if (requestTokenHeader != null ) {
			jwtToken =requestTokenHeader;
			try {
				username = jwtTokenUtil.getUserName(jwtToken);
			} catch (Exception e) {
			} 
		}

		// Once we get the token validate it.
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

			List<Object> accessList = jwtTokenUtil.getAccessList(jwtToken);
			List<GrantedAuthority> authoritiesList = new ArrayList<GrantedAuthority>();
			
			for(Object access: accessList)
			{
				GrantedAuthority authority = null;
				String accessStr = (String) access;
				if(ApplicationConstants.AUTHORITY_ADMIN.equalsIgnoreCase(accessStr))
				{
					authority = new SimpleGrantedAuthority(ApplicationConstants.AUTHORITY_ADMIN);
				}else if(ApplicationConstants.AUTHORITY_HR.equalsIgnoreCase(accessStr))
				{
					authority = new SimpleGrantedAuthority(ApplicationConstants.AUTHORITY_HR);
				}else if(ApplicationConstants.AUTHORITY_SELECTOR.equalsIgnoreCase(accessStr))
				{
					authority = new SimpleGrantedAuthority(ApplicationConstants.AUTHORITY_SELECTOR);
				}else if(ApplicationConstants.AUTHORITY_EXTERNAL.equalsIgnoreCase(accessStr))
				{
					authority = new SimpleGrantedAuthority(ApplicationConstants.AUTHORITY_EXTERNAL);
				}else 
				{
					authority = new SimpleGrantedAuthority(ApplicationConstants.AUTHORITY_INTERVIEWER);
				}
				
				authoritiesList.add(authority);	
			}
			
			// if token is valid configure Spring Security to manually set
			// authentication

			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
					userDetails, null, authoritiesList);
			usernamePasswordAuthenticationToken
					.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			// After setting the Authentication in the context, we specify
			// that the current user is authenticated. So it passes the
			// Spring Security Configurations successfully.
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		}
		chain.doFilter(request, response);
	}

}
