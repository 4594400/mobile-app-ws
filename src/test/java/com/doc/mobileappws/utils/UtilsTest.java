package com.doc.mobileappws.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class) // for JUnit 5
@SpringBootTest
class UtilsTest {

    @Autowired
    Utils utils;

    @BeforeEach
    void setUp() {
    }

    @Test
    void generateUserId() {
        String userId = utils.generateUserId(30);
        String userId2 = utils.generateUserId(30);

        assertNotNull(userId);
        assertTrue(userId.length() == 30);
        assertTrue(!userId.equalsIgnoreCase(userId2));
    }

    @Test
    //@Disabled
    void hasTokenNotExpired() {
        String token = utils.generateEmailVerificationToken("sd868sd879s7d");
        assertNotNull(token);

        boolean hasTokenExpired = Utils.hasTokenExpired(token);
        assertFalse(hasTokenExpired);
    }

    @Test
    void hasTokenExpired() {
        String expiredToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0YW1hem9ud3NAZ21haWwuY29tIiwiZXhwIjoxNjA3NDQ2NDIzfQ.EUD7aJJSAB_P9pLaGl54ImCv1zOSJ22y9QsauCf6K9-RKgAElHLVfFYdAlp_mQL1dvFGbLI04Wqqp0tqmQxLXA";
        boolean hasTokenExpired = Utils.hasTokenExpired(expiredToken);
        assertTrue(hasTokenExpired);
    }

}