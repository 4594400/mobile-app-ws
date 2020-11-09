package com.doc.mobileappws.secutity;

import com.doc.mobileappws.SpringApplicationContext;

public class SecurityConstants {
    public static final long EXPIRATION_TIME = 864000000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users";

    public static String getTokenSecret(){
        ApplicationProperties applicationProperties = (ApplicationProperties) SpringApplicationContext.getBean("applicationProperties");
        return applicationProperties.getTokenSecret();
    }
}
