/**
 * 
 */
package com.zomato.online.cities.resource;

import java.util.*;

import org.jvnet.hk2.annotations.Service;

@Service
public class CitiesImpl implements Cities {

	List<String> cities;

	@Override
	public List<String> getCities() {
		return cities;
	}

	@Override
	public void setCities(List<String> cities) {
		this.cities = cities;
	}
}
