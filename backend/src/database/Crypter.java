package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;

class Crypter {

	private File keyDir = new File(".keys");
	private File pubKey = new File(keyDir, "pub_key");
	private File privKey = new File(keyDir, "priv_key");
	private final String ALGORITHM = "RSA", CRYPT_ALGORITHM = "RSA/ECB/OAEPWithSHA1AndMGF1Padding";
	private RSAPrivateKey privateKey;
	private RSAPublicKey publicKey;
	
	Crypter() {
		generateKeys();
	}
	
	boolean match(char[] input, byte[] encryptedPass) {
		// Compares the input with the encrypted password
		return Arrays.equals(toBytes(input), decrypt(encryptedPass));
	}
	
	byte[] encrypt(char[] input) {
		try {
			Cipher cipher = Cipher.getInstance(CRYPT_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return cipher.doFinal(toBytes(input));
		} 
		catch (Exception e) {
			return null;
		}
	}
	
	byte[] decrypt(byte[] input) {
		
		try {
			Cipher cipher = Cipher.getInstance(CRYPT_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return cipher.doFinal(input);
		} 
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private byte[] toBytes(char[] chars) {
		// Converts input char array to a byte array, for safer encryption
		CharBuffer charBuffer = CharBuffer.wrap(chars);
		ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
		
		byte[] bytes = Arrays.copyOfRange(byteBuffer.array(), byteBuffer.position(), byteBuffer.limit());
		Arrays.fill(byteBuffer.array(), (byte) 0); 
		return bytes;
	}
	
	RSAPublicKey getPublicKey() {
		return publicKey;
	}
	
	private void generateKeys() {
		// Creates new keys of they can't be found, otherwise the keys are loaded from storage
		if (!keyDir.exists()) {
			keyDir.mkdir();
			
			try {
				// Generates a keypair with RSA algorithm and byte-size of 2048
				KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
				keyGen.initialize(2048);
				KeyPair keyPair = keyGen.generateKeyPair();
				privateKey = (RSAPrivateKey) keyPair.getPrivate();
				publicKey = (RSAPublicKey) keyPair.getPublic();
				
				// Stores the generated keys in seperate files
				FileOutputStream writer = new FileOutputStream(pubKey);
				writer.write(publicKey.getEncoded());
				writer.close();
				
				writer = new FileOutputStream(privKey);
				writer.write(privateKey.getEncoded());
				writer.close();
				
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
			
		}

		else {
			try {
				// Loads the keys
				FileInputStream reader = new FileInputStream(pubKey);								// Read stored bytes from public key
				X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(reader.readAllBytes()); 		// Key spec for public key
				reader.close();
				
				reader = new FileInputStream(privKey);												// Read stored bytes from private key
				PKCS8EncodedKeySpec privkeySpec = new PKCS8EncodedKeySpec(reader.readAllBytes());	// Key specification
				reader.close();
				
				
				KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);							// Used to re-create the keys
				publicKey = (RSAPublicKey) keyFactory.generatePublic(pubKeySpec);				
				privateKey = (RSAPrivateKey) keyFactory.generatePrivate(privkeySpec);

			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
