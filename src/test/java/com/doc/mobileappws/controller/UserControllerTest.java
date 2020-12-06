package com.doc.mobileappws.controller;

import com.doc.mobileappws.dto.AddressDto;
import com.doc.mobileappws.dto.UserDto;
import com.doc.mobileappws.model.response.UserRest;
import com.doc.mobileappws.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.omg.PortableInterceptor.USER_EXCEPTION;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserControllerTest {
    final String USER_ID = "jh324jg34jh43kj4h";
    @InjectMocks
    UserController userController;
    @Mock
    UserService userService;

    UserDto userDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        userDto = new UserDto();
        userDto.setFirstName("Sergey");
        userDto.setLastName("Ivanov");
        userDto.setEmail("4amazonws@gmail.com");
        userDto.setEmailVerificationStatus(Boolean.FALSE);
        userDto.setEmailVerificationToken(null);
        userDto.setUserId(USER_ID);
        userDto.setAddresses(getAddressesDto());
        userDto.setEncryptedPassword("lk34kl23klj4");

    }

    @Test
    void getUser() {
        when(userService.getUserByUserId(anyString())).thenReturn(userDto);
        UserRest userRest = userController.getUser(USER_ID);

        assertNotNull(userRest);
        assertEquals(USER_ID, userRest.getUserId());
        assertEquals(userDto.getFirstName(), userRest.getFirstName());
        assertEquals(userDto.getLastName(), userRest.getLastName());
        assertTrue(userDto.getAddresses().size() == userRest.getAddresses().size());
    }


    private List<AddressDto> getAddressesDto(){
        AddressDto addressDto = new AddressDto();
        addressDto.setType("shipping");
        addressDto.setCity("Vancouver");
        addressDto.setCountry("Canada");
        addressDto.setPostalCode("ABC123");
        addressDto.setStreetName("123 Street name");

        AddressDto billingAddressDto = new AddressDto();
        billingAddressDto.setType("billling");
        billingAddressDto.setCity("Vancouver");
        billingAddressDto.setCountry("Canada");
        billingAddressDto.setPostalCode("ABC123");
        billingAddressDto.setStreetName("123 Street name");

        List<AddressDto> addresses = new ArrayList<>();
        addresses.add(addressDto);
        addresses.add(billingAddressDto);
        return addresses;
    }
}