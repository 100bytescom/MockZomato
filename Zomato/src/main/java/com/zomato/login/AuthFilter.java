/**
 * 
 */
package com.zomato.login;

import java.io.*;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;

import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;

import com.zomato.Config;
import com.zomato.Constants;
import com.zomato.ExecutionContext;
import com.zomato.bootstrap.SigningKeys;


/**
 * Authorization filter - inspect all REST endpoint requests for valid access token 
 * @author ashok
 */
public class AuthFilter implements ContainerRequestFilter	{

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String token = requestContext.getHeaderString("Authorization");
		if (token == null || !token.startsWith("Bearer "))	{
			throw new IOException("UnAuthorized!! Make sure right access token passed");
		}
		
		token = token.split("\\s+", 2)[1];
		
		SigningKeys keys = Config.getServiceLocator().getService(SigningKeys.class);
		JwtConsumer tokenValidation = new JwtConsumerBuilder()
				.setExpectedIssuer(Constants.ISSUER)
				.setExpectedAudience(Constants.AUDIENCE)
				.setVerificationKey(keys.getPublicKey())
				.setRequireExpirationTime()
				.build();
		JwtClaims claims = null;
		try {
			 claims = tokenValidation.processToClaims(token);
		} catch (InvalidJwtException e) {
			throw new IOException("UnAuthorized!! Make sure right access token passed");
		}
		
		try {
			ExecutionContext context = new ExecutionContext(Config.getServiceLocator(), claims.getSubject());
			ExecutionContext.set(context);
			System.out.println("LOGIN SUCCEEDED!! for " + claims.getSubject());
		} catch (MalformedClaimException e) {
			throw new IOException("UnAuthorized!! Make sure right access token passed");
		}
	}

}
