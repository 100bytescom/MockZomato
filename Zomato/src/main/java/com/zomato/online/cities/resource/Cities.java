package com.zomato.online.cities.resource;

import java.util.*;

import org.jvnet.hk2.annotations.Contract;

@Contract
public interface Cities {

	public List<String> getCities();

	public void setCities(List<String> cities);
}
