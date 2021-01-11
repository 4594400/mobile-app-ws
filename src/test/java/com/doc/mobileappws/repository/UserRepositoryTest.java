package com.doc.mobileappws.repository;

import com.doc.mobileappws.entity.AddressEntity;
import com.doc.mobileappws.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class) // To get Spring environment available, communicate with DB
@SpringBootTest
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    static boolean recordsCreated = false;

    @BeforeEach
    void setUp() {
        if(!recordsCreated) {
            createRecords();
        }

    }



    @Test
    void testGetVerifiedUsers() {
        Pageable pageableRequest = PageRequest.of(0, 2);
        Page<UserEntity> page = userRepository.findAllUsersWithConfirmedEmailAddress(pageableRequest);
        assertNotNull(page);

        System.out.println("page.getSize() = " + page.getSize());

        List<UserEntity> userEntities = page.getContent();
        assertNotNull(userEntities);
        assertTrue(userEntities.size() == 2);
    }

    @Test
    void testFindUserByFirstName() {
        String firstName = "Sergey";
        List<UserEntity> users = userRepository.findUserByFirstName(firstName);

        assertNotNull(users);
        assertTrue(users.size() == 2);

        UserEntity user = users.get(0);
        assertTrue(user.getFirstName().equals(firstName));
    }

    @Test
    void testFindUserByLastName() {
        String lastName = "Marley";
        List<UserEntity> users = userRepository.findUserByLastName(lastName);

        assertNotNull(users);
        assertTrue(users.size() == 2);

        UserEntity user = users.get(0);
        assertTrue(user.getLastName().equals(lastName));
    }

    @Test
    void testFindUserByKeyword() {
        String keyword = "Mar";
        List<UserEntity> users = userRepository.findUserByKeyword(keyword);

        assertNotNull(users);
        assertTrue(users.size() == 2);

        UserEntity user = users.get(0);
        assertTrue(user.getLastName().contains(keyword) ||
                user.getFirstName().contains(keyword));
    }

    @Test
    void testFindUserFirstNameAndLastNameByKeyword() {
        String keyword = "Mar";
        List<Object[]> users = userRepository.findUserFirstNameAndLastNameByKeyword(keyword);

        assertNotNull(users);
        assertTrue(users.size() == 2);

        Object[] user = users.get(0);
        assertTrue(user.length == 2);

        String userFirstName = String.valueOf(user[0]);
        String userLastName = String.valueOf(user[1]);

        assertNotNull(userFirstName);
        assertNotNull(userLastName);

        System.out.println("FirstName = " + userFirstName);
        System.out.println("LastName = " + userLastName);

    }

    @Test
    // mySQL doesn't have boolean type
    void testUpdateUserEmailVerificationStatus(){
        boolean newEmailVerificationStatus = false;
        userRepository.updateUserEmailVerificationStatus(newEmailVerificationStatus, "1a2b3c");
        UserEntity storedUserDetails = userRepository.findByUserId("1a2b3c");

        boolean storedEmailVerificationStatus = storedUserDetails.getEmailVerificationStatus();
        assertTrue(storedEmailVerificationStatus == newEmailVerificationStatus);
    }



    private void createRecords() {
        // Prepare User Entity
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName("Sergey");
        userEntity.setLastName("Marley");
        userEntity.setUserId("1a2b3c");
        userEntity.setEncryptedPassword("xxx");
        userEntity.setEmail("4amzonws@gmail.com");
        userEntity.setEmailVerificationStatus(true);

        // Prepare User Addresses
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setType("shipping");
        addressEntity.setAddressId("ahgyt74hfy");
        addressEntity.setCity("Vancouver");
        addressEntity.setCountry("Canada");
        addressEntity.setPostalCode("ABCCDA");
        addressEntity.setStreetName("123 Street Address");

        List<AddressEntity> addresses = new ArrayList<>();
        addresses.add(addressEntity);

        userEntity.setAddresses(addresses);

        userRepository.save(userEntity);


        // Prepare User Entity
        UserEntity userEntity2 = new UserEntity();
        userEntity2.setFirstName("Sergey");
        userEntity2.setLastName("Marley");
        userEntity2.setUserId("1a2b3cddddd");
        userEntity2.setEncryptedPassword("xxx");
        userEntity2.setEmail("4amzonws@gmail.com");
        userEntity2.setEmailVerificationStatus(true);

        // Prepare User Addresses
        AddressEntity addressEntity2 = new AddressEntity();
        addressEntity2.setType("shipping");
        addressEntity2.setAddressId("ahgyt74hfywwww");
        addressEntity2.setCity("Vancouver");
        addressEntity2.setCountry("Canada");
        addressEntity2.setPostalCode("ABCCDA");
        addressEntity2.setStreetName("123 Street Address");

        List<AddressEntity> addresses2 = new ArrayList<>();
        addresses2.add(addressEntity2);

        userEntity2.setAddresses(addresses2);

        userRepository.save(userEntity2);

        recordsCreated = true;
    }
}