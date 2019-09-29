/**
 * 
 */
package com.zomato.online.resturants.controller;

import java.io.*;
import java.util.*;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.process.internal.RequestScoped;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zomato.online.resturants.resource.Restaurant;
import com.zomato.online.zomatoclient.ZomatoClient;

/**
 * Class to cater Restaurant requests
 * @author ashok
 */

@RequestScoped
@Path("/resturants")
public class ResturantController {

	@Inject
	private ZomatoClient zomatoClient;
	
	@GET
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getResturants(@QueryParam("type") String type, 
			@QueryParam("value") String value,
			@QueryParam("page") String page,
			@QueryParam("sort") String sort,
			@QueryParam("order") String order) throws JsonParseException, JsonMappingException, IOException {
		String searchUrl = "/search";
		Map<String, String> queryParam = new HashMap<String, String>();
		queryParam.put("entity_type", type);
		queryParam.put("q", value);
		if (page != null) {
			int pageNo = Integer.parseInt(page);
			queryParam.put("start", String.valueOf(pageNo * 10));
		}
		queryParam.put("sort", sort);
		queryParam.put("order", order);
		queryParam.put("count", "10");
		Response response = zomatoClient.get(searchUrl, queryParam);
		String resp = response.readEntity(String.class);
		System.out.println(resp);
		ObjectMapper om = new ObjectMapper();
		Map<String, Object> rest = om.readValue(resp, new TypeReference<Map<String, Object>>(){});
		
		List resturants = (List) rest.get("restaurants");
		List<Restaurant> ress = new ArrayList<Restaurant>();
		for(Object obj :resturants)	{
			Map details = ((Map)((Map)obj).get("restaurant"));
			Restaurant res = new Restaurant();
			res.setId((String) details.get("id"));
			res.setName((String) details.get("name"));
			res.setCuisines((String) details.get("cuisines"));
			res.setTiminigs((String) details.get("timings"));
			res.setAvgCost((Integer) details.get("average_cost_for_two"));
			res.setAddress((String) ((Map)details.get("location")).get("address"));
			Object rating = ((Map)details.get("user_rating")).get("aggregate_rating");
			String rat = null;
			if (rating instanceof Integer) rat = String.valueOf(rating);
			else rat = (String)rating;
			
			res.setAvgRating(rat);
			ress.add(res);
		}
		String restResp = om.writeValueAsString(ress);
		System.out.println(restResp);
		Response resp1 = Response.ok(restResp).build();
		return resp1;
	}
}
