package com.doc.mobileappws.service.impl;

import com.doc.mobileappws.dto.AddressDto;
import com.doc.mobileappws.entity.AddressEntity;
import com.doc.mobileappws.entity.UserEntity;
import com.doc.mobileappws.repository.AddressRepository;
import com.doc.mobileappws.repository.UserRepository;
import com.doc.mobileappws.service.AddressService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public List<AddressDto> getAddresses(String userId) {
        List<AddressDto> returnValue = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        UserEntity userEntity = userRepository.findByUserId(userId);
        if(userEntity == null) return returnValue;

        Iterable<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);

        for (AddressEntity addressEntity : addresses) {
            returnValue.add(modelMapper.map(addressEntity, AddressDto.class));
        }

        return returnValue;
    }

    @Override
    public AddressDto getAddress(String addressId) {
        AddressDto returnValue = null;
        AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
        if(addressEntity != null){
            returnValue = new ModelMapper().map(addressEntity, AddressDto.class);
        }
        return returnValue;
    }
}
