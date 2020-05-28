package hr.fer.zemris.java.hw06.crypto;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {
	/**
	 * Metoda od koje počinje izvođenje programa.
	 * 
	 * @param args argumenti komandne linije
	 * @throws InvalidAlgorithmParameterException 
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 * @throws IOException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException, IOException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		if(args.length < 1) {
			throw new UnsupportedOperationException("Unsupported command.");
		}
		
		switch(args[0]) {
			case "checksha":
				if(args.length != 2) {
					throw new IllegalArgumentException("Command checksha expects 1 argument.");
				}
				checksha(args[1]);
				break;
				
			case "encrypt":
			case "decrypt":
				if(args.length != 3) {
					throw new IllegalArgumentException("Command " + args[0] + " expects 2 arguments.");
				}
				encryptOrDecrypt(args[0], args[1], args[2]);
				break;
				
			default:
				throw new UnsupportedOperationException("Unsupported command.");
		}
	}
	
	/**
	 * Metoda za provjeru originalost datoteke koja prima ime datoteke
	 * i od korisnika traži očekivani SHA-256 digest.
	 * Metoda korisniku ispisuje poruku poklapaju li se ili ne stvari i 
	 * očekivani SHA-256 digest.
	 * 
	 * @param fileName ime datoteke
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	private static void checksha(String fileName) throws IOException, NoSuchAlgorithmException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Please provide expected sha-256 digest for " + fileName + ":");
		System.out.printf("> ");
		String expectedSha = sc.next();
		sc.close();
		
		try {
			byte[] expectedShaBytes = Util.hextobyte(expectedSha);
			MessageDigest sha = MessageDigest.getInstance("SHA-256");
			InputStream inStream = Files.newInputStream(Paths.get(fileName));
							
			byte[] bytes = new byte[4000];
			while(inStream.available() >= 4000) {
				inStream.read(bytes, 0, 4000);
				sha.update(bytes);
			}
			
			bytes = new byte[inStream.available()];
			inStream.read(bytes, 0, inStream.available());
			byte[] realShaBytes = sha.digest(bytes);
					
			if(Util.bytetohex(realShaBytes).equals(Util.bytetohex(expectedShaBytes))) {
				System.out.println("Digesting completed. Digest of " + fileName + " matches expected digest.");
			} else {
				System.out.println("Digesting completed. Digest of " + fileName + " doesn't match expected digest. Digest was: " 
						 + Util.bytetohex(realShaBytes));
			}
		} catch(IllegalArgumentException ex) {
			System.out.println(ex.getMessage());
		}		
	}
	
	/**
	 * Metoda za enkripciju ili dekripciju datoteke.
	 * Metoda će od korisnika tražiti lozinku koju želi
	 * postaviti za enkripciju ili lozinku potrebnu za dekripciju.
	 * 
	 * @param mode encrypt ili decrypt
	 * @param fileName ime datoteke
	 * @param outputFileName ime output datoteke
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IOException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	private static void encryptOrDecrypt(String mode, String fileName, String outputFileName) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IOException, IllegalBlockSizeException, BadPaddingException {
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):");
		System.out.printf("> ");
		String keyText = sc.next();
		System.out.println("Please provide initialization vector as hex-encoded text (32 hex-digits):");
		System.out.printf("> ");
		String ivText = sc.next();
		
		sc.close();
		
		boolean encrypt = mode.equals("encrypt");
		
		SecretKeySpec keySpec = new SecretKeySpec(Util.hextobyte(keyText), "AES");
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(Util.hextobyte(ivText));
		
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, paramSpec);
		
		InputStream inStream = Files.newInputStream(Paths.get(fileName));
		OutputStream outStream = Files.newOutputStream(Paths.get(outputFileName));
		
		byte[] bytes = new byte[4000];
		while(inStream.available() >= 4000) {
			inStream.read(bytes, 0, 4000);
			outStream.write(cipher.update(bytes));
		}
		
		bytes = new byte[inStream.available()];
		inStream.read(bytes, 0, inStream.available());
		outStream.write(cipher.doFinal(bytes));
		
		if(encrypt) {
			System.out.println("Encryption completed. Generated file " + outputFileName + " based on file " + fileName + ".");
		} else {
			System.out.println("Decryption completed. Generated file " + outputFileName + " based on file " + fileName + ".");
		}
	}
}
