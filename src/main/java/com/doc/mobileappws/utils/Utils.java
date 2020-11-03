package com.doc.mobileappws.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class Utils {
    private final Random RANDOM = new SecureRandom();
    private final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public String generateUserId(int length){
        return generateRandomString(length);
    }

    private String generateRandomString(int length) {
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < length; i++){
            result.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return new String(result);
    }
}
