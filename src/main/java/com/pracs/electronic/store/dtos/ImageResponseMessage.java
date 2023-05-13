package com.pracs.electronic.store.dtos;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageResponseMessage {

    private String imageName;
    private String message;
    private boolean success;
    private HttpStatus httpStatus;

}
