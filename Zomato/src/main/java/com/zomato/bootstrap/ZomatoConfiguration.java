/**
 * 
 */
package com.zomato.bootstrap;


/**
 * Class hold Zomato configuration
 * @author ashok
 */
public class ZomatoConfiguration {

	private String url;
	private char[] apiKey;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public char[] getApiKey() {
		return apiKey;
	}
	public void setApiKey(char[] apiKey) {
		this.apiKey = apiKey;
	}
}
