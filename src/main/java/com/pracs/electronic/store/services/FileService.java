package com.pracs.electronic.store.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface FileService {

    // general function to upload any image - user, product etc.
    String uploadFile(MultipartFile file, String path) throws IOException;

    // returns the resource on the given path in the form of Input String
    InputStream getResource(String path, String name) throws FileNotFoundException;

}
