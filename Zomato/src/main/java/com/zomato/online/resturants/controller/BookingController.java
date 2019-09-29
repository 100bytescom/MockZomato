/**
 * 
 */
package com.zomato.online.resturants.controller;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;

import javax.inject.Singleton;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.process.internal.RequestScoped;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zomato.ExecutionContext;
import com.zomato.online.resturants.resource.Booking;


/**
 * Class to cater Booking requests
 * 
 * @author ashok
 */
@RequestScoped
@Singleton
@Path("/booking")
public class BookingController {

	Map<String, List<Booking>> bookings = new HashMap<String, List<Booking>>();
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response bookit(Booking book) throws JsonProcessingException	{
		int status = 200;
		String msg = "";
		if (book.getGuests() > 20) {
			msg = "Max allowed guests 20";
			status = 400;
		}
		else	{
			String subject = ExecutionContext.get().getSubject();
			List<Booking> books = bookings.get(subject);
			if (books == null)	{
				books = new ArrayList<Booking>();
				bookings.put(subject, books);
			}
			book.setId(UUID.randomUUID().toString());
			if (books.contains(book))	{
				status = 409;
				msg = "Booking already made";
			}
			else	{
				ObjectMapper om = new ObjectMapper();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				om.setDateFormat(dateFormat);
				msg = om.writeValueAsString(book);
				System.out.println(book + " " + msg);
				books.add(book);
			}
		}
		return Response.ok(msg).status(status).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response mybookings() throws JsonProcessingException {
		String subject = ExecutionContext.get().getSubject();
		List<Booking> books = bookings.get(subject);
		String msg = "[]";
		if (books != null)	{
			ObjectMapper om = new ObjectMapper();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			om.setDateFormat(dateFormat);
			msg = om.writeValueAsString(books);
		}
		
		Response response = Response.ok(msg).build();
		return response;
	}
	
	
	@DELETE
	@Path(value = "/{id}")
	public Response cancelBooking(@PathParam("id") String id)  {
		String subject = ExecutionContext.get().getSubject();
		List<Booking> books = bookings.get(subject);
		int status = 204;
		String msg = "";
		if (books == null)	{
			status = 404;
			msg = "No bookings found"; 
		}
		else {
			Predicate<Booking> idFilter = book -> book.getId().equalsIgnoreCase(id);
			if (!books.removeIf(idFilter))	{
				status = 404;
				msg = "Requested resource doesn't exist";
			}
		}
		
		Response response = Response.ok(msg).status(status).build();
		return response;
	}
}
