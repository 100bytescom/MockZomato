/**
 * 
 */
package com.zomato.bootstrap;

import java.io.*;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.ServerConfiguration;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.zomato.Config;
import com.zomato.Constants;
import com.zomato.ZomExceptionMapper;
import com.zomato.login.AuthFilter;
import com.zomato.login.authzflow.Authz;
import com.zomato.login.authzflow.Redirector;
import com.zomato.online.cities.controller.CitiesController;
import com.zomato.online.resturants.controller.BookingController;
import com.zomato.online.resturants.controller.FavouriteController;
import com.zomato.online.resturants.controller.ResturantController;



class Resources extends ResourceConfig	{
	
	public Resources()	{
		
		//Authorization filter
		register(AuthFilter.class);
		
		//Business logic REST endpoints
		register(CitiesController.class);
		register(ResturantController.class);
		register(FavouriteController.class);
		register(BookingController.class);
		
		//Custom exception mapper
		register(ZomExceptionMapper.class);
	}
}


/**
 * Main Gate to the application
 * @author ashok
 */
public class MainGate {

	public static void main(String a[]) throws URISyntaxException, IOException	{
		
		new Configurations();
		
		ResourceConfig rc = new Resources();
		URI uri = new URI("http://" + InetAddress.getLocalHost().getCanonicalHostName() + ":80");
		HttpServer server = GrizzlyHttpServerFactory.createHttpServer(uri, rc, Config.getServiceLocator());
		
		ServerConfiguration sc = server.getServerConfiguration();
		sc.addHttpHandler(new Authz(), Constants.LOGIN_PAGE);
		sc.addHttpHandler(new Redirector(), Constants.AUTHZ_REDIRECT_URL);
		sc.addHttpHandler(new StaticHttpHandler(new File("").getAbsolutePath() + File.separator 
				+ "resources" + File.separator), "/resources");
		
		System.out.println(server.toString());
		System.out.println(uri.toURL().toString());
		server.start();
	}
}
