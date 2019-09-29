/**
 * 
 */
package com.zomato.login.authzflow;

import java.net.URL;
import java.util.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zomato.Config;
import com.zomato.Constants;
import com.zomato.bootstrap.SigningKeys;
import com.zomato.login.IdpConfiguration;


/**
 * Class caters redirect flow for authentication
 * 
 * Note: This class is a public endpoint
 * @author ashok
 */
public class Redirector extends HttpHandler	{

	@Override
	public void service(Request request, Response response) throws Exception {

		IdpConfiguration idpConfiguration = Config.getServiceLocator().getService(IdpConfiguration.class);
		
		URL url1 = new URL(request.getRequestURL().toString());
		URL redirectUrl = new URL(url1, Constants.AUTHZ_REDIRECT_URL);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(Constants.REDIRECT_URI, redirectUrl.toString()));
		params.add(new BasicNameValuePair(Constants.GRANT_TYPE, Constants.AUTHORIZATION_CODE));
		params.add(new BasicNameValuePair(Constants.CLIENT_ID, idpConfiguration.getClientId()));
		params.add(new BasicNameValuePair(Constants.CLIENT_SECRET, new String(idpConfiguration.getClientCreds())));
		params.add(new BasicNameValuePair(Constants.CODE, request.getParameter(Constants.CODE)));
		
		UrlEncodedFormEntity encodedUrl = new UrlEncodedFormEntity(params);
		byte[] url = new byte[(int) encodedUrl.getContentLength()];
		encodedUrl.getContent().read(url);
		
		Client client = ClientBuilder.newClient();
		javax.ws.rs.core.Response idpResponse = client.target(idpConfiguration.getTokenEndpoint())
				.request()
				.post(Entity.entity(new String(url), MediaType.APPLICATION_FORM_URLENCODED));
		String postResponse = idpResponse.readEntity(String.class);
		//TODO verify signature of IDP response using IDP's JWK's
		
		ObjectMapper om = new ObjectMapper();
		Map<String, String> idpResponseValues = om.readValue(postResponse, new TypeReference<Map<String, String>>(){});
		String accessToken = idpResponseValues.get("access_token");
		client = ClientBuilder.newClient();
		javax.ws.rs.core.Response userResponse = client.target(idpConfiguration.getUserInfoEndpoint())
												.request()
												.header("Authorization", "Bearer " + accessToken)
												.get();
		Map<String, String> userDetails = om.readValue(userResponse.readEntity(String.class), new TypeReference<Map<String, String>>(){});
		
		String userName = userDetails.get("name");
		String emailId = userDetails.get("email");
		StringBuilder sb = new StringBuilder();
		sb.append("Hello " + userName);
		sb.append("\n" + emailId);
		response.getWriter().write(sb.toString() + "\n");
		
		JwtClaims claims = new JwtClaims();
		claims.setAudience(Constants.AUDIENCE);
		claims.setIssuer(Constants.ISSUER);
		claims.setIssuedAtToNow();
		claims.setExpirationTimeMinutesInTheFuture(60);
		claims.setSubject(userDetails.get("email"));
		claims.setClaim("name", userDetails.get("name"));
		claims.setGeneratedJwtId();
		
		SigningKeys keys = Config.getServiceLocator().getService(SigningKeys.class);
		JsonWebSignature jws = new JsonWebSignature();
		jws.setKey(keys.getPrivateKey());
		jws.setPayload(claims.toJson());
		jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA512);
		
		response.getWriter().write("\n\nAccess Token :: " + jws.getCompactSerialization() + "\n");

		System.out.println(jws.getCompactSerialization());
		System.out.println(new String(Base64.getEncoder().encode(keys.getPublicKey().getEncoded())));
	}

}
