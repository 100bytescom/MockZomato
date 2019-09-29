/**
 * 
 */
package com.zomato.login.authzflow;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;


import org.apache.http.client.utils.URIBuilder;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

import com.zomato.Config;
import com.zomato.Constants;
import com.zomato.login.IdpConfiguration;


/**
 * Class to trigger 3-legged Authz flow
 * 
 * Note: This is a public endpoint
 * @author ashok
 */
public class Authz extends HttpHandler {

	@Override
	public void service(Request request, Response response) throws Exception {
		String serverLoc = new File("").getAbsolutePath();
		File f = new File(serverLoc + File.separator + Constants.RESOURCES_DIR + File.separator + "token.html");
		BufferedReader br = new BufferedReader(new FileReader(f));
		String s = null;
		StringBuffer sb = new StringBuffer();
		while((s = br.readLine()) != null)	{
			if (s.contains("##LOGIN_URL##")) sb.append(s.replaceAll("##LOGIN_URL##", getSigninUrl(request)));
			else sb.append(s);
		}
		response.getWriter().write(sb.toString());
		br.close();
	}

	private String getSigninUrl(Request request) throws URISyntaxException, MalformedURLException	{
		
		IdpConfiguration idpConfiguration = Config.getServiceLocator().getService(IdpConfiguration.class);
		//TODO pass state and nonce paramenter for more security
		URL url1 = new URL(request.getRequestURL().toString());
		URL redirectUrl = new URL(url1, Constants.AUTHZ_REDIRECT_URL);
		URI uri = new URIBuilder(idpConfiguration.getAuthzEndpoint())
				.addParameter(Constants.REDIRECT_URI, redirectUrl.toString())
				.addParameter(Constants.RESPONSE_TYPE, Constants.CODE)
				.addParameter(Constants.SCOPE, Constants.LOGIN_SCOPE)
				.addParameter(Constants.CLIENT_ID, idpConfiguration.getClientId())
				.addParameter(Constants.PROMPT, Constants.CONSENT)
				.build();
		return uri.toString();
	}
}
