/**
 * 
 */
package com.zomato;

import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Customoized exception mapper
 * @author ashok
 */
public class ZomExceptionMapper implements ExceptionMapper<Exception>	{

	@Override
	public Response toResponse(Exception exception) {
		
		int status = 500;
		String msg = exception.getLocalizedMessage();
		if (msg != null && msg.contains("UnAuthorized")) status = 401;
		else if (exception instanceof NotFoundException) {
			msg = "Whoa!! Unavailable!";
			status = 404;
		}
		else if (exception instanceof NotAllowedException)	{
			status = 405;
			msg = "You're almost right! Use right HTTP method";
		}
		
		return Response.ok(msg).status(status).type(MediaType.TEXT_PLAIN).build();
	}

}
