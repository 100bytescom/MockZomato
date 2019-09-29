/**
 * 
 */
package com.zomato;

import org.glassfish.hk2.api.ServiceLocator;

/**
 * Holds service locator dependency injection
 * @author ashok
 */
public class Config {
	private static ServiceLocator serviceLocator;

	public static ServiceLocator getServiceLocator() {
		return serviceLocator;
	}
	
	public static void setServiceLocator(ServiceLocator sl) {
		serviceLocator = sl;
	}
}
