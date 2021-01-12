package com.doc.mobileappws.controller;

import com.doc.mobileappws.dto.AddressDto;
import com.doc.mobileappws.dto.UserDto;
import com.doc.mobileappws.model.request.PasswordResetModel;
import com.doc.mobileappws.model.request.PasswordResetRequestModel;
import com.doc.mobileappws.model.request.UserDetailsRequestModel;
import com.doc.mobileappws.model.response.*;
import com.doc.mobileappws.service.AddressService;
import com.doc.mobileappws.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/users")
//@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:8081"})
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AddressService addressService;

    /**
     * localhost:8080/users/?page=0&limit=50
     * after adding context-path: localhost:8080/mobile-app-ws/users/?page=2
     * @param page
     * @param limit
     * @return
     */
    @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "limit", defaultValue = "2") int limit){
        List<UserRest> returnValue = new ArrayList<>();
        List<UserDto> users = userService.getUsers(page, limit);
        for(UserDto userDto : users){
            UserRest userRest = new UserRest();
            BeanUtils.copyProperties(userDto, userRest);
            returnValue.add(userRest);
        }

        return returnValue;
    }

    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_XML_VALUE,  // first MediaType it is default returning type
            MediaType.APPLICATION_JSON_VALUE})
    public UserRest getUser(@PathVariable String id) {
        UserRest returnValue = new UserRest();
        UserDto userDto = userService.getUserByUserId(id);
        ModelMapper modelMapper = new ModelMapper();
        returnValue = modelMapper.map(userDto, UserRest.class);
        return returnValue;
    }

    /** Request
     * <UserDetailsRequestModel>
     *     <firstName>Andrei</firstName>
     *     <lastName>Ivanov</lastName>
     *     <email>dealer@gmail.com</email>
     *     <password>12345678</password>
     * </UserDetailsRequestModel>
     * @param userDetails
     * @return
     */
    // first MediaType it is default returning type
    @PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE, MediaType  .APPLICATION_JSON_VALUE},
                 produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
                 )
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) {
        UserRest returnValue = new UserRest();
        //if(userDetails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        if(userDetails.getFirstName().isEmpty()) throw new NullPointerException("The object is null");

        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        UserDto createdUser = userService.createUser(userDto);
        returnValue = modelMapper.map(createdUser, UserRest.class);

        return returnValue;
    }

    @PutMapping(path = "/{id}",
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {
        UserDto userDto = new ModelMapper().map(userDetails, UserDto.class);
        UserDto updateUser = userService.updateUser(id, userDto);

        UserRest returnValue = new ModelMapper().map(updateUser, UserRest.class);

        return returnValue;
    }

    @DeleteMapping(path = "/{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public OperationStatusModel deleteUser(@PathVariable String id) {
        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.DELETE.name());
        userService.deleteUser(id);
        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());

        return returnValue;
    }

    // localhost:8080/mobile-app-ws/users/6RsBi6oekKiZbEre5LjzwtWpT0qpoW/addresses
    @GetMapping(path = "/{id}/addresses", produces = {MediaType.APPLICATION_XML_VALUE,  // first MediaType it is default returning type
            MediaType.APPLICATION_JSON_VALUE})
    public CollectionModel<AddressesRest> getUserAddresses(@PathVariable String id) {
        List<AddressesRest> returnValue = new ArrayList<>();
        List<AddressDto> addressDto = addressService.getAddresses(id);

        if(addressDto != null && !addressDto.isEmpty()) {
            Type listType = new TypeToken<List<AddressesRest>>() {}.getType();
            returnValue = new ModelMapper().map(addressDto, listType);

            for(AddressesRest addressRest : returnValue){
                Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                        .getUserAddress(id, addressRest.getAddressId()))
                        .withSelfRel();
                addressRest.add(selfLink);
            }
        }

        Link userLink = WebMvcLinkBuilder.linkTo(UserController.class).slash(id).withRel("user");
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddresses(id)).withSelfRel();

        return new CollectionModel(returnValue, userLink, selfLink);
    }

    //localhost:8080/mobile-app-ws/users/YmTGq5lo9vD2jdEZWHl7kRH7ndtzl4/addresses/FqaepwYNuXGKoWYF6nHw52rB2YSIy3
    @GetMapping(path = "/{userId}/addresses/{addressId}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public EntityModel<AddressesRest> getUserAddress(@PathVariable String userId, @PathVariable String addressId){
        AddressDto addressDto = addressService.getAddress(addressId);
        ModelMapper modelMapper = new ModelMapper();
        AddressesRest returnValue = modelMapper.map(addressDto, AddressesRest.class);

        // "http://localhost:8080/mobile-app-ws/users/YmTGq5lo9vD2jdEZWHl7kRH7ndtzl4"
        Link userLink = WebMvcLinkBuilder.linkTo(UserController.class)
                .slash(userId)
                .withRel("user");

        //  "http://localhost:8080/mobile-app-ws/users/YmTGq5lo9vD2jdEZWHl7kRH7ndtzl4/addresses"
        Link userAddressesLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddresses(userId))
                //.slash(userId)
                //.slash("addresses")
                .withRel("addresses");

        //   "http://localhost:8080/mobile-app-ws/users/YmTGq5lo9vD2jdEZWHl7kRH7ndtzl4/addresses/myiYTxY9nnPegQi4Tsnqchr2kCufbF"
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddress(userId, addressId))
                //.slash(userId)
                //.slash("addresses")
                //.slash(addressId)
                .withSelfRel();

        return new EntityModel(returnValue, Arrays.asList(userLink, userAddressesLink, selfLink));
    }

    // "http://localhost:8080/mobile-app-ws/users/email-verification/token=sdsdsd"
    @GetMapping(path = "/email-verification", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public OperationStatusModel verifyEmailToken(@RequestParam(value = "token") String token){
        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.VERIFY_EMAIL.name());

        boolean isVerified = userService.verifyEmailToken(token);
        if(isVerified){
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        }else {
            returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
        }
        return returnValue;

    }

    /**
     * Reset password
     *      http://localhost:8080/mobile-app-ws/users/password-reset-request
     */
    @PostMapping(path = "/password-reset-request",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public OperationStatusModel requestReset(@RequestBody PasswordResetRequestModel passwordResetRequestModel){
        OperationStatusModel returnValue = new OperationStatusModel();
        boolean operationResult = userService.requestPasswordReset(passwordResetRequestModel.getEmail());

        returnValue.setOperationName(RequestOperationName.REQUEST_PASSWORD_RESET.name());
        returnValue.setOperationResult(RequestOperationStatus.ERROR.name());

        if(operationResult){
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        }
        return returnValue;
    }

    /**
     * Reset password
     *      http://localhost:8080/mobile-app-ws/users/password-reset
     */
    @PostMapping(path = "/password-reset", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE} )
    public OperationStatusModel resetPassword(@RequestBody PasswordResetModel passwordResetModel){
        OperationStatusModel returnValue = new OperationStatusModel();
        boolean operationResult = userService.requestPassword(passwordResetModel.getToken(), passwordResetModel.getPassword());

        returnValue.setOperationName(RequestOperationName.PASSWORD_RESET.name());
        returnValue.setOperationResult(RequestOperationStatus.ERROR.name());

        if(operationResult){
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        }
        return returnValue;
    }

}
