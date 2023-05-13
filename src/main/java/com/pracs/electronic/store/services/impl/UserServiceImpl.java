package com.pracs.electronic.store.services.impl;

import com.pracs.electronic.store.dtos.PageableResponse;
import com.pracs.electronic.store.dtos.UserDto;
import com.pracs.electronic.store.entities.User;
import com.pracs.electronic.store.exceptions.ResourceNotFoundException;
import com.pracs.electronic.store.helper.Helper;
import com.pracs.electronic.store.repositories.UserRepository;
import com.pracs.electronic.store.services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public UserDto createUser(UserDto userDto) {

        // generate user ID in string format
        String userId = UUID.randomUUID().toString();

        userDto.setUserId(userId);
        // DTO TO ENTITY
        User user = dtoToEntity(userDto);
        User savedUser = userRepository.save(user);

        // ENTITY TO DTO
        UserDto savedUserDto = entityToDto(savedUser);

        return savedUserDto;
    }



    @Override
    public UserDto updateUser(UserDto userDto, String userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with the given ID"));
        user.setName(userDto.getName());
        //update email if needed
        user.setAbout(userDto.getAbout());
        user.setPassword(userDto.getPassword());
        user.setImageName(userDto.getImageName());

        User updatedUser = userRepository.save(user);
        UserDto updatedUserDto = entityToDto(updatedUser);
        return updatedUserDto;
    }

    @Override
    public void deleteUser(String userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with the given ID"));
        userRepository.delete(user);

    }

    @Override
    public PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir) {

//        logger.info("Value of sortDir = {}", sortDir);
        Sort sort = (sortDir.trim().equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());

        Pageable pageable = PageRequest.of(pageNumber-1, pageSize, sort);

        Page<User> all = userRepository.findAll(pageable);

        PageableResponse<UserDto> response = Helper.getPageableResponse(all, UserDto.class);

        return response;

        // below code is only for getting page response with list of User
        /*
         Pageable pageable = PageRequest.of(pageNumber, pageSize);
         Page<User> pageable = userRepository.findAll(pageable);
         List<User> allUsers = pageable.getContent();

         List<UserDto> allUsersDto = allUsers.stream().map( user -> entityToDto(user)).collect(Collectors.toList())
         */

    }

    @Override
    public UserDto getUserById(String userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with the given ID"));
        UserDto userDto = entityToDto(user);
        return userDto;
    }

    @Override
    public UserDto getUserByEmail(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(("User not found with given id and password")));
        UserDto userDto = entityToDto(user);
        return userDto;
    }

    @Override
    public List<UserDto> searchUser(String keyword) {

        List<User> usersByNameContaining = userRepository.findByNameContaining(keyword);

        List<UserDto> usersContainingName = usersByNameContaining.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
        return usersContainingName;
    }
    private UserDto entityToDto(User savedUser) {

//        UserDto userDto = UserDto.builder()
//                .userId(savedUser.getUserId())
//                .name(savedUser.getName())
//                .email(savedUser.getEmail())
//                .password(savedUser.getPassword())
//                .about(savedUser.getAbout())
//                .gender(savedUser.getGender())
//                .imageName(savedUser.getImageName()).build();

        return modelMapper.map(savedUser, UserDto.class);
    }

    private User dtoToEntity(UserDto userDto) {
//        User user = User.builder()
//                .userId(userDto.getUserId())
//                .name(userDto.getName())
//                .email(userDto.getEmail())
//                .password(userDto.getPassword())
//                .about(userDto.getAbout())
//                .imageName(userDto.getImageName())
//                .gender(userDto.getGender()).build();
        return modelMapper.map(userDto, User.class);

    }
}
