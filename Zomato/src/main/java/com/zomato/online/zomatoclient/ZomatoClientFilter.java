/**
 * 
 */
package com.zomato.online.zomatoclient;

import java.io.*;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

import com.zomato.Config;
import com.zomato.Constants;
import com.zomato.bootstrap.ZomatoConfiguration;


/**
 * Zomato client filter to inject api key when making upstream requests(to Zomato server)
 * @author ashok
 */
public class ZomatoClientFilter implements ClientRequestFilter	{

	@Override
	public void filter(ClientRequestContext requestContext) throws IOException {
		
		System.out.println("CLIENT_REQUEST_FILTER");
		ZomatoConfiguration zomatoConfig = Config.getServiceLocator().getService(ZomatoConfiguration.class);
		requestContext.getHeaders().add(Constants.USER_KEY, new String(zomatoConfig.getApiKey()));
	}

}
