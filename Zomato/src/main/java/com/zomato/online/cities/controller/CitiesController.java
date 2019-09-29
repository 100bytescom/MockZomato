/**
 * 
 */
package com.zomato.online.cities.controller;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.process.internal.RequestScoped;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zomato.online.cities.resource.Cities;


/**
 * Controller class to get available cities
 * @author ashok
 */

@Path("/cities")
@Singleton
@RequestScoped
public class CitiesController {

	@Inject
	private Cities cities;

	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCities() throws JsonProcessingException	{
		System.out.println("INSIDE");
		ObjectMapper om = new ObjectMapper();
		String city = om.writeValueAsString(cities.getCities());
		return Response.ok(city).build();
	}
}
