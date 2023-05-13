package com.pracs.electronic.store.dtos;

import com.pracs.electronic.store.validate.ImageNameValid;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private String userId;

    @Size(min = 3, max = 50, message = "Invalid Name!!")
    private String name;

//    @Email(message = "Invalid User Email!!")
    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+[a-z]{2,5}$", message = "Invalid user email")
    private String email;

    @NotBlank(message = "Password is required!!")
    private String password;

    @Size(min = 4, max = 10)
    private String gender;

    @NotBlank(message = "Write something about yourself!!")
    private String about;

    @ImageNameValid
    private String imageName;

    //@Pattern
    //@Custom validator
}
