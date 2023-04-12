package com.portal.utils;

import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.portal.ApplicationConfigurations;
import com.portal.ApplicationConstants;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil {

	@Autowired
	private ApplicationConfigurations applicationConfigurations;
	
	
	public static JSONObject decodeUserToken(String token) {
		String[] chunks = token.split("\\.");
		Base64.Decoder decoder = Base64.getUrlDecoder();
		String body = new String(decoder.decode(chunks[1]));
		JSONObject jsonObject = new JSONObject(body);
		return jsonObject;
	}
	
	public String getUserName(String token)
	{
		JSONObject object = decodeUserToken(token);
		if(object != null)
			return object.getString("userName");
		else 
			return null;
	}
	
	public  List<Object> getAccessList(String token)
	{
		JSONObject object = decodeUserToken(token);
		JSONArray accessList = null;
		if(object != null)
			accessList = object.getJSONArray("access");
		if(accessList != null)
			return accessList.toList();
		
		return null;
		
	}
	
	public String generateToken(String userName, String userId,List<String> access, String type,String microsoftId) {
		Calendar currentTimeNow = Calendar.getInstance();
		currentTimeNow.add(Calendar.MINUTE, 30);
		Date expireTime = currentTimeNow.getTime();
		JwtBuilder token = Jwts.builder().setIssuer("DIGISPRINT").setSubject("access").setHeaderParam("typ", "JWT")
				.claim("userName", userName).claim("userId", userId).claim("type", type).claim("access",access).claim(ApplicationConstants.MICROSOFT_ID, microsoftId)
				.setIssuedAt(currentTimeNow.getTime()).setExpiration(expireTime)
				.signWith(SignatureAlgorithm.HS256, applicationConfigurations.getSecretKey().getBytes());

		return token.compact();
	}
}
