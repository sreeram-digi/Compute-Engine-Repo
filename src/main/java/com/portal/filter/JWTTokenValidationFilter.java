package com.portal.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portal.bean.Interviewer;
import com.portal.repository.InterviewerRepository;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import io.jsonwebtoken.impl.crypto.JwtSignatureValidator;
import lombok.extern.slf4j.Slf4j;

import org.json.JSONObject;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

@Component
@Slf4j
@Order(1)
public class JWTTokenValidationFilter implements Filter {

	
	
	private InterviewerRepository interviewerRepository;
	
	@Value("${portal.configuration.secretKey}")
	private String secretKey;

	public JWTTokenValidationFilter(InterviewerRepository interviewerRepository) {
		this.interviewerRepository = interviewerRepository;
	}

	private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		insertInitialData();
		Filter.super.init(filterConfig);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		log.debug("Cutom Filter- doFilter");

		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse=(HttpServletResponse) response;
		httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
		
		String token = httpServletRequest.getHeader("token");
		if (token != null && !((HttpServletRequest) request).getRequestURI().contains("login") 
				&& !((HttpServletRequest) request).getRequestURI().contains("swagger")
				&& !((HttpServletRequest) request).getRequestURI().contains("api-docs")
				&& !((HttpServletRequest) request).getRequestURI().contains("actuator")) {
			Base64.Decoder decoder = Base64.getUrlDecoder();
			SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS256.getJcaName());
			JwtSignatureValidator validator = new DefaultJwtSignatureValidator(SignatureAlgorithm.HS256, secretKeySpec);
			String[] chunks = token.split("\\.");
			if (chunks.length < 2) {
				Exception e = new Exception("seems you doesnt have proper permissions");
				ObjectMapper mapper = new ObjectMapper();
				((HttpServletResponse) response).setStatus(HttpStatus.UNAUTHORIZED.value());
				((HttpServletResponse) response).sendError(HttpStatus.UNAUTHORIZED.value(),"Not Authorised to use the request");
			} else {
				String tokenWithoutSignature = chunks[0] + "." + chunks[1];
				String signature = chunks[2];
				if (!validator.isValid(tokenWithoutSignature, signature)) {
					Exception e = new Exception("User is already loggedIn");
					ObjectMapper mapper = new ObjectMapper();
					response.getWriter().write(mapper.writeValueAsString(e));
					((HttpServletResponse) response).sendError(HttpStatus.UNAUTHORIZED.value(),"Not Authorised to use the request");
				}else {
					String body = new String(decoder.decode(chunks[1]));
					JSONObject jsonObject = new JSONObject(body);
					JSONArray array = (JSONArray) jsonObject.get("access");
					
					chain.doFilter(request, response);
				}
			}
		}else if (((HttpServletRequest) request).getRequestURI().contains("login") 
				|| ((HttpServletRequest) request).getRequestURI().contains("swagger")
				|| ((HttpServletRequest) request).getRequestURI().contains("api-docs")) {
			chain.doFilter(request, response);
		} else {
			Exception e = new Exception("Not allowed to use the Service");
			ObjectMapper mapper = new ObjectMapper();
			((HttpServletResponse) response).setStatus(HttpStatus.UNAUTHORIZED.value());
			((HttpServletResponse) response).sendError(HttpStatus.UNAUTHORIZED.value(),"Not Authorised to use the request");
		}
	}
	private void insertInitialData() {
		log.info("initial Data loaded");
		if(!interviewerRepository.findById("Admin").isPresent()) {
			Interviewer admin = new Interviewer();
			admin.setInterviewerName("Admin");
			admin.setInterviewerEmail("admin@admin.com");
			admin.setInterviewerPassword("Admin@123");
			admin.setAdmin(true);
			admin.setHr(true);
			admin.setSelector(true);
			admin.setId("Admin");
			interviewerRepository.insert(admin);
		}
	}

}
