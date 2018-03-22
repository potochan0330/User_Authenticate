package authenticate_user;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class authenticate_user {
	public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String acc="", pw="", signcheck="";
		byte[] hpw = null;
		ArrayList<String> tempacc = new ArrayList<String>();
		ArrayList<String> temppw = new ArrayList<String>();
		ArrayList<String> tempcheck = new ArrayList<String>();
		String fileName = "list.txt";
			/******
			 * reference from https://www.caveofprogramming.com/java/java-file-reading-and-writing-files-in-java.html
			 */
	        String line = null;
	        try {
	            FileReader fileReader = new FileReader(fileName);
	            BufferedReader bufferedReader = new BufferedReader(fileReader);

		        while((line = bufferedReader.readLine()) != null) {
		        	String[] temptext = line.split("\t");
		            tempacc.add(temptext[0]);	
		            temppw.add(temptext[1]);
		            tempcheck.add(temptext[2]);
		        }
		        bufferedReader.close();
	        }
	        catch(FileNotFoundException ex) {
	            System.out.println("Unable to open file '" + fileName + "'");                
	        }
	        catch(IOException ex) {
	            System.out.println("Error reading file '" + fileName + "'");
	        }
		
		Scanner scanner = new Scanner(System.in);
	    System.out.println("Please input username:");
	    acc = scanner.nextLine();
       	System.out.println("Please input password:");
    	pw = scanner.nextLine();
    	
    	MessageDigest md = MessageDigest.getInstance("SHA-256");
    	md.update(pw.getBytes("UTF-8"));
	    hpw = md.digest();
    	
	    try{   	
	        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
	        SecretKeySpec secret_key = new SecretKeySpec(acc.getBytes(), "HmacSHA256");
	        sha256_HMAC.init(secret_key);
	        byte[] check = new byte[acc.getBytes().length + hpw.length];
	        System.arraycopy(acc.getBytes(), 0, check, 0, acc.getBytes().length);
	        System.arraycopy(hpw, 0, check, acc.getBytes().length, hpw.length);
	        signcheck = String.format("%064x", new java.math.BigInteger(1,sha256_HMAC.doFinal(check)));
	    }
	    catch (Exception e){
	    	System.out.println("Error");
	    }
            	
		if(tempacc.contains(acc)){
		   	if(String.format("%064x", new java.math.BigInteger(1,hpw)).equals(temppw.get(tempacc.indexOf(acc)))){
		   		if(signcheck.equals(tempcheck.get(tempacc.indexOf(acc))))
		   			System.out.println("Welcome to the system, "+acc+"!");
		   		else
		       		System.out.println("The text file is modified.");
		   	}
		   	else{
		   		System.out.println("Authentication fail.");
		   	}
		}
	   else{
	    	System.out.println("Authentication fail.");
	   }    
	}
}
