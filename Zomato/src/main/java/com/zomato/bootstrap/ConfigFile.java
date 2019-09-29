/**
 * 
 */
package com.zomato.bootstrap;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO represntation of configuration file 
 * @author ashok
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfigFile {

	private static final String OPENID = "openId";
	private static final String IDPNAME = "idpName";
	private static final String CONFIGURL = "configUrl";
	private static final String APIKEY = "apiKey";
	private static final String CREDENTIALS = "credentials";
	private static final String CITIES = "cities";
	private static final String URL = "url";
	private static final String ZOMATOCONFIG = "zomatoConfig";
	
	@JsonProperty(value = OPENID)
	private OpenId openId;
	
	@JsonProperty(value = CITIES)
	private List<String> cities;
	
	@JsonProperty(value = ZOMATOCONFIG)
	private ZomatoConfig zomatoConfig;
	
	public class OpenId	{
		@JsonProperty(value = IDPNAME)
		private String idpName;
		@JsonProperty(value = CONFIGURL)
		private String configUrl;
		@JsonProperty(value = APIKEY)
		private String apiKey;
		@JsonProperty(value = CREDENTIALS)
		private char[] credentials;
		
		public String getIdpName() {
			return idpName;
		}
		public void setIdpName(String idpName) {
			this.idpName = idpName;
		}
		public String getConfigUrl() {
			return configUrl;
		}
		public String getApiKey() {
			return apiKey;
		}
		public void setConfigUrl(String configUrl) {
			this.configUrl = configUrl;
		}
		public void setApiKey(String apiKey) {
			this.apiKey = apiKey;
		}
		public char[] getCredentials() {
			return credentials;
		}
		
		public void setCredentials(char[] credentials) {
			this.credentials = credentials;
		}
		@Override
		public String toString() {
			return "OpenId [idpName=" + idpName + ", configUrl=" + configUrl + ", apiKey=" + apiKey + ", credentials="
					+ new String(credentials) + "]";
		}
		
	}
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	public class ZomatoConfig {

		@JsonProperty(value = URL)
		private String url;
		@JsonProperty(value = APIKEY)
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
		@Override
		public String toString() {
			return "ZomatoConfig [url=" + url + ", apiKey=" + Arrays.toString(apiKey) + "]";
		}
		
	}

	public OpenId getOpenId() {
		return openId;
	}

	public void setOpenId(OpenId openId) {
		this.openId = openId;
	}

	public List<String> getCities() {
		return cities;
	}

	public void setCities(List<String> cities) {
		this.cities = cities;
	}

	public ZomatoConfig getZomatoConfig() {
		return zomatoConfig;
	}

	public void setZomatoConfig(ZomatoConfig zomatoConfig) {
		this.zomatoConfig = zomatoConfig;
	}

	@Override
	public String toString() {
		return "ConfigFile [openId=" + openId + ", cities=" + cities + ", zomatoConfig=" + zomatoConfig + "]";
	}

	
}
