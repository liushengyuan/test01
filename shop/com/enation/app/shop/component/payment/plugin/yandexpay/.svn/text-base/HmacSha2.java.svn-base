package com.enation.app.shop.component.payment.plugin.yandexpay;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;


public class HmacSha2 {
	public static String hmacSha2(String value, String key) {
		try {
			byte[] keyBytes = key.getBytes();
			SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA256");
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(signingKey);
			byte[] rawHmac = mac.doFinal(value.getBytes("UTF-8")); 
			byte[] hexBytes = new Hex().encode(rawHmac);
			return new String(hexBytes, "UTF-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

        public static String hmacSha2(String value, String key, String encoding) {
            try {
                    byte[] keyBytes = key.getBytes();
                    SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA256");
                    Mac mac = Mac.getInstance("HmacSHA256");
                    mac.init(signingKey);
                    byte[] rawHmac = mac.doFinal(value.getBytes(encoding)); 
                    byte[] hexBytes = new Hex().encode(rawHmac);
                    return new String(hexBytes, encoding);
            } catch (Exception e) {
                    throw new RuntimeException(e);
            }
    }
	public static void main(String[] args) {
		String a = "0.020";
		String b = "test";
		//System.out.println("src:" + a);
	        //System.out.println("key:" + b);
		//System.out.println(hmacSha2(a, b));
	        //System.out.println(hmacSha2(a, b, "UTF-8"));
	        //System.out.println(hmacSha2(a, b, "GBK"));
	}
}

