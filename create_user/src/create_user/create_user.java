package create_user;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;



public class create_user {
	public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException,InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
		String acc="", pw="", ch, sign="";
		byte[] hpw = null;
		ArrayList<String> tempacc = new ArrayList<String>();
		boolean close = false;
		String fileName = "list.txt";
		/*
		 * Generate the pair RSA key
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        byte[] publicKey = keyGen.genKeyPair().getPublic().getEncoded();
        byte[] privateKey = keyGen.genKeyPair().getPrivate().getEncoded();
        StringBuffer pubKey = new StringBuffer();
        for (int i = 0; i < publicKey.length; ++i) {
            pubKey.append(Integer.toHexString(0x0100 + (publicKey[i] & 0x00FF)).substring(1));
        }
        StringBuffer priKey = new StringBuffer();
        for (int j = 0; j < privateKey.length; ++j) {
        	priKey.append(Integer.toHexString(0x0100 + (privateKey[j] & 0x00FF)).substring(1));
        }
        System.out.println(pubKey);
        System.out.println(priKey);
		*/
		
		while(!close){
			
			/* read the text file
			 * reference from https://www.caveofprogramming.com/java/java-file-reading-and-writing-files-in-java.html
			 */
	        String line = null;
	        try {
	            FileReader fileReader = new FileReader(fileName);
	            BufferedReader bufferedReader = new BufferedReader(fileReader);

		        while((line = bufferedReader.readLine()) != null) {
		            tempacc.add(line.substring(0,line.indexOf("\t")));	
		        }
		        bufferedReader.close();
	        }
	        catch(FileNotFoundException ex) {
	            System.out.println("Unable to open file '" + fileName + "'");                
	        }
	        catch(IOException ex) {
	            System.out.println("Error reading file '" + fileName + "'"); 
	        }

	        
			/*	get and check input
			 * 	reference from http://www.javapractices.com/topic/TopicAction.do?Id=42
			 */
			Scanner scanner = new Scanner(System.in);
		    System.out.println("Please input username:");
		    acc = scanner.nextLine();
		    while(tempacc.contains(acc)){
		    	System.out.println("Username has been used, please enter new one:");
		    	acc = scanner.nextLine();
		    }
	       	System.out.println("Please input password:");
	    	pw = scanner.nextLine();
	    	while(pw.length()<6){	    	
	    		System.out.println("The length of password must longer than or equal 6.");
	    		System.out.println("Please input password:");
		    	pw = scanner.nextLine();
	    	}
	    		    		   	
	    	MessageDigest md = MessageDigest.getInstance("SHA-256");
	    	md.update(pw.getBytes("UTF-8"));
		    hpw = md.digest();
		    
		    /* create a check string to prevent the text file is modified by others
		     * use Hmca-Sha256 to create the signature 
		     * reference from http://www.jokecamp.com/blog/examples-of-creating-base64-hashes-using-hmac-sha256-in-different-languages/
		     */
		    try{   	
		        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
		        SecretKeySpec secret_key = new SecretKeySpec(acc.getBytes(), "HmacSHA256");
		        sha256_HMAC.init(secret_key);
		        byte[] check = new byte[acc.getBytes().length + hpw.length];
		        System.arraycopy(acc.getBytes(), 0, check, 0, acc.getBytes().length);
		        System.arraycopy(hpw, 0, check, acc.getBytes().length, hpw.length);
		        sign = String.format("%064x", new java.math.BigInteger(1,sha256_HMAC.doFinal(check)));
		        }
		       catch (Exception e){
		        System.out.println("Error");
		       }
		    /*	write the username in the text file
		     * 	write the hashed password in the text file
		     * 	write the check number in the text file
		     * 	ask the admin terminate the system or not
		     * 	reference from http://stackoverflow.com/questions/1625234/how-to-append-text-to-an-existing-file-in-java
		     */
			try{
				FileWriter filewriter = new FileWriter(fileName, true);
				BufferedWriter bufferedWriter = new BufferedWriter(filewriter);
				bufferedWriter.write(acc);
			    bufferedWriter.write("\t");
			    bufferedWriter.write(String.format("%064x", new java.math.BigInteger(1,hpw)));
			    bufferedWriter.write("\t");
			    bufferedWriter.write(sign);
			    bufferedWriter.newLine();
			 	System.out.println("Anymore user need to create?(Y/N)");
			 	ch= scanner.next();
			  	switch(ch){
				  	case "y":
				  		close =  false;
				  		break;
				  	case "Y":
				  		close =  false;
				  		break;
				  	case "N":
				  		close =  true;
				  		System.out.println("System terminate...");
				  		break;
				  	case "n":
				  		close =  true;
				  		System.out.println("System terminate...");
				  		break;
				  	default:
						System.out.println("Invalid Input. System terminate...");
				  		close =  true;
				  		break;
			  	}
				        bufferedWriter.close();
			}
			catch(IOException ex){
				System.out.println("Error writing to file '"+ fileName + "'");
			}
		}
	}
}
