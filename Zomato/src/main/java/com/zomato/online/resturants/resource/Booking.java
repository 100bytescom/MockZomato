/**
 * 
 */
package com.zomato.online.resturants.resource;

import java.time.Instant;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Booking {

	@JsonProperty
	private String id;
	@JsonProperty(required = true, value= "restaurantId")
	private int restaurantId;
	@JsonProperty(required = true, value= "time")
	private Date time;
	@JsonProperty(required = true, value= "status")
	private Status status;
	@JsonProperty(required = true, value= "guests")
	private int guests;
	
	public enum Status	{
		ACTIVE,
		CANCELLED,
		DONE
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getResturantId() {
		return restaurantId;
	}

	public void setResturantId(int resturantId) {
		this.restaurantId = resturantId;
	}

	public Date getBookingTime() {
		return time;
	}

	public void setBookingTime(Date bookingTime) {
		this.time = bookingTime;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public int getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(int restaurantId) {
		this.restaurantId = restaurantId;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public int getGuests() {
		return guests;
	}

	public void setGuests(int guests) {
		this.guests = guests;
	}

		

	@Override
	public String toString() {
		return "Booking [id=" + id + ", restaurantId=" + restaurantId + ", time=" + time + ", status=" + status
				+ ", guests=" + guests + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Booking other = (Booking) obj;
		
		
		if (restaurantId != other.restaurantId)
			return false;
		if (status != other.status)
			return false;

		long t1 = time.toInstant().getEpochSecond();
		long t2 = other.time.toInstant().getEpochSecond();
		if (Math.abs(t1 - t2) >= 3600) return false;
		return true;
	}
	
	
	
}
