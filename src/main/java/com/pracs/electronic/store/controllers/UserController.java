package com.pracs.electronic.store.controllers;

import com.pracs.electronic.store.dtos.ApiResponseMessage;
import com.pracs.electronic.store.dtos.ImageResponseMessage;
import com.pracs.electronic.store.dtos.PageableResponse;
import com.pracs.electronic.store.dtos.UserDto;
import com.pracs.electronic.store.services.FileService;
import com.pracs.electronic.store.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;
    //create
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
        UserDto createdUserDto = userService.createUser(userDto);
//        return ResponseEntity.ok(createdUserDto);
        return new ResponseEntity<>(createdUserDto, HttpStatus.CREATED);
    }


    //update
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable String userId,
            @Valid @RequestBody UserDto userDto){

        UserDto updatedUserDto = userService.updateUser(userDto, userId);
        return new ResponseEntity<>(updatedUserDto, HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable("userId") String userId){
        userService.deleteUser(userId);
        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                .message("User is deleted successfully")
                .success(true)
                .httpStatus(HttpStatus.OK).build();
        return new ResponseEntity<>(apiResponseMessage, HttpStatus.OK);
    }

    //getAll
    @GetMapping
    public ResponseEntity<PageableResponse<UserDto>> getAllUsers(
            @RequestParam(value = "pageNumber", defaultValue = "1", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir)
    {
        return new ResponseEntity<>(userService.getAllUsers(pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    //get single
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getSingleUserById(@PathVariable("userId") String userId){
        UserDto userById = userService.getUserById(userId);
        return new ResponseEntity<>(userById, HttpStatus.OK);
    }

    //get by email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getSingleUserByEmail(@PathVariable("email") String userEmail){
        return new ResponseEntity<>(userService.getUserByEmail(userEmail), HttpStatus.OK);
    }

    //search user
    @GetMapping("/search/{keywords}")
    public ResponseEntity<List<UserDto>> getUsersContainingKeyword(@PathVariable("keywords") String keywords){
        return ResponseEntity.ok(userService.searchUser(keywords));
    }

    // upload user image

    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponseMessage> uploadUserImage(@RequestParam("userImage")MultipartFile userImage, @PathVariable String userId) throws IOException {

        String imageName = fileService.uploadFile(userImage, imageUploadPath);
        UserDto user = userService.getUserById(userId);
        user.setImageName(imageName);

        userService.updateUser(user, userId);


        ImageResponseMessage imageResponse = ImageResponseMessage.builder()
                .imageName(imageName)
                .message("Image uploaded successfully")
                .success(true)
                .httpStatus(HttpStatus.CREATED).build();

        return new ResponseEntity<>(imageResponse, HttpStatus.CREATED);

    }

    //serve user image
}
