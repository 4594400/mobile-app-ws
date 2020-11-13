package com.doc.mobileappws.service;

import com.doc.mobileappws.dto.AddressDto;

import java.util.List;

public interface AddressesService {
    List<AddressDto> getAddresses(String userId);
}
