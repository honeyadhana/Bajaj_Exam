package com.bajaj.main;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Random;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class BajajHealthMain {
		    
		    public static void main(String[] args) {
		        if (args.length != 2) {
		            System.out.println("Usage: java -jar DestinationHashGenerator.jar <PRN_Number> <JSON_File_Path>");
		            return;
		        }

		        String prnNumber = args[0].toLowerCase().replaceAll("\\s", "");
		        String jsonFilePath = args[1];

		        try {
		            // Step 2: Read and Parse JSON File
		            JSONObject jsonObject = new JSONObject(new JSONTokener(new FileReader(jsonFilePath)));

		            // Step 3: Traverse JSON for "destination"
		            String destinationValue = findDestinationValue(jsonObject);
		            if (destinationValue == null) {
		                System.out.println("No 'destination' key found in JSON file.");
		                return;
		            }

		            // Step 4: Generate Random Alphanumeric String
		            String randomString = generateRandomString(8);

		            // Step 5: Compute MD5 Hash
		            String concatenatedString = prnNumber + destinationValue + randomString;
		            String md5Hash = computeMD5Hash(concatenatedString);

		            // Step 6: Format the Output
		            String output = md5Hash + ";" + randomString;
		            System.out.println(output);

		        } catch (IOException e) {
		            System.err.println("Error reading JSON file: " + e.getMessage());
		        } catch (NoSuchAlgorithmException e) {
		            System.err.println("Error generating MD5 hash: " + e.getMessage());
		        }
		    }

		    // Step 3: Recursively find the first occurrence of "destination"
		    private static String findDestinationValue(JSONObject jsonObject) {
		        Iterator<String> keys = jsonObject.keys();
		        while (keys.hasNext()) {
		            String key = keys.next();
		            Object value = jsonObject.get(key);

		            if (key.equals("destination")) {
		                return value.toString();
		            } else if (value instanceof JSONObject) {
		                String result = findDestinationValue((JSONObject) value);
		                if (result != null) {
		                    return result;
		                }
		            } else if (value instanceof JSONArray) {
		                JSONArray jsonArray = (JSONArray) value;
		                for (int i = 0; i < jsonArray.length(); i++) {
		                    if (jsonArray.get(i) instanceof JSONObject) {
		                        String result = findDestinationValue(jsonArray.getJSONObject(i));
		                        if (result != null) {
		                            return result;
		                        }
		                    }
		                }
		            }
		        }
		        return null;
		    }

		    // Step 4: Generate Random Alphanumeric String
		    private static String generateRandomString(int length) {
		        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		        Random random = new Random();
		        StringBuilder sb = new StringBuilder();
		        for (int i = 0; i < length; i++) {
		            sb.append(characters.charAt(random.nextInt(characters.length())));
		        }
		        return sb.toString();
		    }

		    // Step 5: Compute MD5 Hash
		    private static String computeMD5Hash(String input) throws NoSuchAlgorithmException {
		        MessageDigest md = MessageDigest.getInstance("MD5");
		        byte[] hashInBytes = md.digest(input.getBytes());
		        StringBuilder sb = new StringBuilder();
		        for (byte b : hashInBytes) {
		            sb.append(String.format("%02x", b));
		        }
		        return sb.toString();
		    
	}

}
