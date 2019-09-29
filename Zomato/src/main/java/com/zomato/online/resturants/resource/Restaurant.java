/**
 * 
 */
package com.zomato.online.resturants.resource;


public class Restaurant {

	private String id;
	private String name;
	private String address;
	private String cuisines;
	private String timinigs;
	private String avgRating;
	private int avgCost;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCuisines() {
		return cuisines;
	}
	public void setCuisines(String cuisines) {
		this.cuisines = cuisines;
	}
	public String getTiminigs() {
		return timinigs;
	}
	public void setTiminigs(String timinigs) {
		this.timinigs = timinigs;
	}
	public String getAvgRating() {
		return avgRating;
	}
	public void setAvgRating(String avgRating) {
		this.avgRating = avgRating;
	}
	public int getAvgCost() {
		return avgCost;
	}
	public void setAvgCost(int avgCost) {
		this.avgCost = avgCost;
	}
}
