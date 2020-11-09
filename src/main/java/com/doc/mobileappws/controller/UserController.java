package com.doc.mobileappws.controller;

import com.doc.mobileappws.dto.UserDto;
import com.doc.mobileappws.exception.UserServiceException;
import com.doc.mobileappws.model.request.UserDetailsRequestModel;
import com.doc.mobileappws.model.response.ErrorMessages;
import com.doc.mobileappws.model.response.UserRest;
import com.doc.mobileappws.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_XML_VALUE,  // first MediaType it is default returning type
            MediaType.APPLICATION_JSON_VALUE})
    public UserRest getUser(@PathVariable String id) {
        UserRest returnValue = new UserRest();
        UserDto userDto = userService.getUserByUserId(id);
        BeanUtils.copyProperties(userDto, returnValue);
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
    @PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
                 produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
                 )
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) {
        UserRest returnValue = new UserRest();
        //if(userDetails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        if(userDetails.getFirstName().isEmpty()) throw new NullPointerException("The object is null");

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto createdUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser, returnValue);

        return returnValue;
    }

    @PutMapping
    public String updateUser() {
        return "update user was called";
    }

    @DeleteMapping
    public String deleteUser() {
        return "delete user was called";
    }
}
