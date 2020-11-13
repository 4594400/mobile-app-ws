package com.doc.mobileappws.repository;

import com.doc.mobileappws.entity.AddressEntity;
import com.doc.mobileappws.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long> {
    List<AddressEntity> findAllByUserDetails(UserEntity userEntity);

}
