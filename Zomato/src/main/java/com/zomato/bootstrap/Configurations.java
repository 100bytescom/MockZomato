/**
 * 
 */
package com.zomato.bootstrap;

import java.io.*;
import java.net.URL;
import java.util.*;

import javax.inject.Singleton;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.hk2.utilities.ServiceLocatorUtilities;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zomato.Config;
import com.zomato.Constants;
import com.zomato.login.IdpConfiguration;
import com.zomato.online.cities.resource.Cities;
import com.zomato.online.cities.resource.CitiesImpl;
import com.zomato.online.zomatoclient.ZomatoClient;


/**
 * Class to load configuration and dependency injection of objects
 * 
 * Note: All the activities are done during server startup
 * @author ashok
 */
public class Configurations {

	
	private void loadConfigurations(ServiceLocator sl) throws JsonParseException, JsonMappingException, IOException	{
		String configFile = Constants.RESOURCES_DIR + File.separator + Constants.CONFIG_FILE;
		
		ObjectMapper objectMapper = new ObjectMapper();
		ConfigFile configuration = objectMapper.readValue(new File(configFile), ConfigFile.class);
		
		//Loading IDP configuration for authentication
		URL idpUrl = new URL(configuration.getOpenId().getConfigUrl());
		//Loading idp configuration from IDP /.well-known/openid-configuration
		Map<String, Object> idpConfiguration = objectMapper.readValue(idpUrl, new TypeReference<Map<String, Object>>(){});
		IdpConfiguration idpConfiguration2 = sl.getService(IdpConfiguration.class);
		idpConfiguration2.setAuthzEndpoint((String) idpConfiguration.get(Constants.AUTHZ_ENDPOINT));
		idpConfiguration2.setTokenEndpoint((String) idpConfiguration.get(Constants.TOKEN_ENDPOINT));
		idpConfiguration2.setUserInfoEndpoint((String) idpConfiguration.get(Constants.USERINFO_ENDPOINT));
		idpConfiguration2.setIssuer((String) idpConfiguration.get(Constants.IDP_ISSUER));
		idpConfiguration2.setClientId(configuration.getOpenId().getApiKey());
		idpConfiguration2.setClientCreds(configuration.getOpenId().getCredentials());
		System.out.println(configuration);
		
		Cities cities = sl.getService(Cities.class);
		cities.setCities(configuration.getCities());
		
		//Zomator related configurations 
		ZomatoConfiguration zomatoConfig = sl.getService(ZomatoConfiguration.class);
		zomatoConfig.setApiKey(configuration.getZomatoConfig().getApiKey());
		zomatoConfig.setUrl(configuration.getZomatoConfig().getUrl());
	}
	
	/**
	 * Loading dependncy injections
	 */
	private void bindInjections()	{
		ServiceLocatorFactory slf = ServiceLocatorFactory.getInstance();
		ServiceLocator sl = slf.create("null");
		ServiceLocatorUtilities.bind(sl, new AbstractBinder() {
			
			@Override
			protected void configure() {
				bind(IdpConfiguration.class).to(IdpConfiguration.class).in(Singleton.class);
				bind(CitiesImpl.class).to(Cities.class).in(Singleton.class);
				bind(SigningKeys.class).to(SigningKeys.class).in(Singleton.class);
				bind(ZomatoConfiguration.class).to(ZomatoConfiguration.class).in(Singleton.class);
				bind(ZomatoClient.class).to(ZomatoClient.class).in(Singleton.class);
			}
		});
		Config.setServiceLocator(sl);
	}
	
	public Configurations() throws JsonParseException, JsonMappingException, IOException	{
		bindInjections();
		ServiceLocator sl = Config.getServiceLocator();
		loadConfigurations(sl);
		System.out.println(sl.getService(Cities.class).getCities());
		System.out.println(sl.getService(ZomatoConfiguration.class).getUrl());
	}
	
}