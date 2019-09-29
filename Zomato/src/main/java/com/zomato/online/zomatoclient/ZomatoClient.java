/**
 * 
 */
package com.zomato.online.zomatoclient;

import java.util.*;

import javax.inject.Inject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;


import com.zomato.Config;
import com.zomato.bootstrap.ZomatoConfiguration;

/**
 * Class to make actual request with Zomato endpoint
 * @author ashok
 */
public class ZomatoClient {

	@Inject
	private ZomatoConfiguration zomatoConfig;
	
	private WebTarget client; 
	
	public ZomatoClient()	{
		zomatoConfig = Config.getServiceLocator().getService(ZomatoConfiguration.class);
		client = ClientBuilder.newClient()
				.target(zomatoConfig.getUrl())
				.register(ZomatoClientFilter.class);
	}
	
	public Response get(String queryUrl, Map<String, String> queryParam)	{
		WebTarget target = client.path(queryUrl);
		for(Map.Entry<String, String> entry : queryParam.entrySet())	{
			target = target.queryParam(entry.getKey(), entry.getValue());
		}
		Response response = target.request().get();
		System.out.println(response);
		return response;
	}
}
