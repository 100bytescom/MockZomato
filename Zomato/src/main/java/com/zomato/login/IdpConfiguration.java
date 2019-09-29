package com.zomato.login;


/**
 * Class to hold IDP configuration
 * @author ashok
 */

public class IdpConfiguration {

	private String idpName;
	private String authzEndpoint;
	private String tokenEndpoint;
	private String userInfoEndpoint;
	private String issuer;
	private String clientId;
	private char[] clientCreds;
	
	public String getIdpName() {
		return idpName;
	}
	public void setIdpName(String idpName) {
		this.idpName = idpName;
	}
	public String getAuthzEndpoint() {
		return authzEndpoint;
	}
	public void setAuthzEndpoint(String authzEndpoint) {
		this.authzEndpoint = authzEndpoint;
	}
	public String getTokenEndpoint() {
		return tokenEndpoint;
	}
	public void setTokenEndpoint(String tokenEndpoint) {
		this.tokenEndpoint = tokenEndpoint;
	}
	public String getUserInfoEndpoint() {
		return userInfoEndpoint;
	}
	public void setUserInfoEndpoint(String userInfoEndpoint) {
		this.userInfoEndpoint = userInfoEndpoint;
	}
	public String getIssuer() {
		return issuer;
	}
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public char[] getClientCreds() {
		return clientCreds;
	}
	public void setClientCreds(char[] clientCreds) {
		this.clientCreds = clientCreds;
	}
	
}
