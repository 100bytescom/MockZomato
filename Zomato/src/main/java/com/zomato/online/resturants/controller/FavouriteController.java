/**
 * 
 */
package com.zomato.online.resturants.controller;

import java.io.*;
import java.util.*;

import javax.inject.Singleton;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.process.internal.RequestScoped;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.zomato.ExecutionContext;


/**
 * Class to cater Favourite requests
 * @author ashok
 */

@Path("/favourite")
@RequestScoped
@Singleton
public class FavouriteController {

	private Map<String, Set<String>> favorites;
	private ObjectMapper om = new ObjectMapper();
	private File favFile;
	
	public FavouriteController() throws JsonParseException, JsonMappingException, FileNotFoundException, IOException {
		String filePath = new File("").getAbsolutePath() + File.separator + "resources" + File.separator + "favourite.json";
		favFile = new File(filePath);
		if (!favFile.exists()) favFile.createNewFile();
		om.configure(Feature.AUTO_CLOSE_SOURCE, true);
		try	{
			favorites = om.readValue(new FileInputStream(favFile), new TypeReference<Map<String, Set<String>>>() {});
		} catch(MismatchedInputException me)	{
			favorites = new HashMap<String, Set<String>>();
		}
	}
	
	@POST
	@Path(value = "/{id}")
	@Produces("application/json")
	public Response favourite(@PathParam("id") String id) throws JsonGenerationException, JsonMappingException, IOException	{
		String subject = ExecutionContext.get().getSubject();
		Set<String> rests = favorites.get(subject);
		if (rests == null) {
			rests = new HashSet<String>();
			favorites.put(subject, rests);
		}
		rests.add(id);
		om.writeValue(favFile, favorites);
		String favs = om.writeValueAsString(rests);
		Response response = Response.ok(favs).build();
		return response;
	}
	
	@GET
	@Produces("application/json")
	public Response getFavorites() throws JsonProcessingException	{
		String subject = ExecutionContext.get().getSubject();
		Set<String> rests = favorites.get(subject);
		String favs = "[]";
		if (rests != null) favs = om.writeValueAsString(rests);
		Response response = Response.ok(favs).build();
		return response;
	}
	
	@DELETE
	@Path(value = "/{id}")
	public Response removeFavourite(@PathParam("id") String id) throws JsonGenerationException, JsonMappingException, IOException {
		String subject = ExecutionContext.get().getSubject();
		Set<String> rests = favorites.get(subject);
		int status = 404;
		String msg = "Requested resources doesn't exist";
		if (rests != null)	{
			if (rests.remove(id)) {
				status = 204;
				om.writeValue(favFile, favorites);
				msg = "";
			}
		}
		Response response = Response.ok(msg).status(status).build();
		return response;
	}
}
