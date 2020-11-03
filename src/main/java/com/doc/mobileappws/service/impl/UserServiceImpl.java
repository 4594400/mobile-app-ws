package com.doc.mobileappws.service.impl;

import com.doc.mobileappws.UserRepository;
import com.doc.mobileappws.dto.UserDto;
import com.doc.mobileappws.entity.UserEntity;
import com.doc.mobileappws.service.UserService;
import com.doc.mobileappws.utils.Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    Utils utils;
    @Override
    public UserDto createUser(UserDto userDto) {
        if(userRepository.findByEmail(userDto.getEmail()) != null){
            throw new RuntimeException("record already exist");
        }


        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDto, userEntity);

        userEntity.setEncryptedPassword("test");
        String publicUserId = utils.generateUserId(30);
        userEntity.setUserId(publicUserId);

        UserEntity storedUserDetails = userRepository.save(userEntity);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(storedUserDetails, returnValue);

        return returnValue;
    }
}
