package com.doc.mobileappws.service.impl;

import com.doc.mobileappws.dto.AddressDto;
import com.doc.mobileappws.dto.UserDto;
import com.doc.mobileappws.entity.AddressEntity;
import com.doc.mobileappws.entity.UserEntity;
import com.doc.mobileappws.exception.UserServiceException;
import com.doc.mobileappws.repository.UserRepository;
import com.doc.mobileappws.utils.AmazonSES;
import com.doc.mobileappws.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    @InjectMocks
    UserServiceImpl userService;
    @Mock
    UserRepository userRepository;
    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    Utils utils;
    @Mock
    AmazonSES amazonSES;

    String userId = "wesdsfd2f5ew4";
    String encryptedPassword = "2323jkjlk2k3h4";
    String email = "4amazonws@gmail.com";
    UserEntity userEntity;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("Sergey");
        userEntity.setLastName("Petrov");
        userEntity.setUserId(userId);
        userEntity.setEncryptedPassword(encryptedPassword);
        userEntity.setEmail("4amazonws@gmail.com");
        userEntity.setEmailVerificationToken("7htnfhr758");
        userEntity.setAddresses(getAddressesEntity());

    }

    @Test
    void getUser() {

        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);
        UserDto userDto = userService.getUser(email);

        assertNotNull(userDto);
        assertEquals("Sergey", userDto.getFirstName());
    }

    @Test
    void getUser_UserNameNotFoundException() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);


        assertThrows(UsernameNotFoundException.class,
                ()->  {
                    userService.getUser("test@test.com");
                });
    }

    @Test
    void createUser_UserServiceException() {
        UserDto userDto = new UserDto();
        userDto.setAddresses(getAddressesDto());
        userDto.setFirstName("Sergey");
        userDto.setLastName("Petrov");
        userDto.setPassword("12345678");
        userDto.setEmail("4amazonws@gmail.com");

        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);


        assertThrows(UserServiceException.class,
                ()->  {
                    userService.createUser(userDto);
                });
    }


    @Test
    void createUser() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(utils.generateAddressId(anyInt())).thenReturn("43kjhnjk3h4");
        when(utils.generateUserId(anyInt())).thenReturn(userId);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encryptedPassword);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        Mockito.doNothing().when(amazonSES).verifyEmail(any(UserDto.class));

        UserDto userDto = new UserDto();
        userDto.setAddresses(getAddressesDto());
        userDto.setFirstName("Sergey");
        userDto.setLastName("Petrov");
        userDto.setPassword("12345678");
        userDto.setEmail("4amazonws@gmail.com");

        UserDto storedUserDetails = userService.createUser(userDto);
        assertNotNull(storedUserDetails);
        assertEquals(userEntity.getFirstName(), storedUserDetails.getFirstName());
        assertEquals(userEntity.getLastName(), storedUserDetails.getLastName());
        assertNotNull(storedUserDetails.getUserId());
        assertEquals(storedUserDetails.getAddresses().size(), userEntity.getAddresses().size());

        verify(utils, times(storedUserDetails.getAddresses().size())).generateAddressId(30);
        verify(bCryptPasswordEncoder, times(1)).encode("12345678");
        verify(userRepository, times(1)).save(any(UserEntity.class));
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

    private List<AddressEntity> getAddressesEntity(){
        List<AddressDto> addresses = getAddressesDto();
        Type listType = new TypeToken<List<AddressEntity>>(){}.getType();
        return new ModelMapper().map(addresses, listType);
    }
}