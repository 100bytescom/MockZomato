/**
 * 
 */
package com.zomato.bootstrap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import com.zomato.Constants;

import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;


/**
 * Class to load and hold the key pair for Signing and verifying JWT
 * @author ashok
 */
public class SigningKeys {

	private PrivateKey privateKey;
	private PublicKey publicKey;
	
	public SigningKeys() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableEntryException	{
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		String filePath = new File("").getAbsolutePath() + File.separator + Constants.RESOURCES_DIR + File.separator + Constants.KEYSTORE_FILE; 
		InputStream jks = new FileInputStream(new File(filePath));
		keyStore.load(jks, "zomato".toCharArray());
		KeyStore.ProtectionParameter password = new KeyStore.PasswordProtection("zomato".toCharArray());
		KeyStore.PrivateKeyEntry privateKeyEntry = (PrivateKeyEntry) keyStore.getEntry("jwtsignkey", password);
		java.security.cert.Certificate cert = keyStore.getCertificate("jwtsignkey");
		this.privateKey = privateKeyEntry.getPrivateKey();
		this.publicKey = cert.getPublicKey();
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}
}
