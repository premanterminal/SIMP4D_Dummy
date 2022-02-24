package com.dispenda.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Hashing {

	private StringBuilder builder;
    public String md5StringFor(String s)
    {
    	MessageDigest digest;
    	builder = new StringBuilder();
		try {
			digest = MessageDigest.getInstance("MD5");
			final byte[] hash = digest.digest(s.getBytes());
	        for (byte b : hash){
	            builder.append(Integer.toString(b & 0xFF, 16));    
	        }
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return builder.toString();
    }
}
