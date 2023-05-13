package com.pracs.electronic.store.services.impl;

import com.pracs.electronic.store.exceptions.BadApiRequestException;
import com.pracs.electronic.store.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {


    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException { // file which is to be uploaded and the path on which it should be uploaded

        String originalFilename = file.getOriginalFilename();
        logger.info("Filename: {}", originalFilename);

        String fileName = UUID.randomUUID().toString(); // to tackle problem of multiple files with same name

        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileNameWithExtension = fileName+extension;
        String fullPathWithFileName = path +     fileNameWithExtension;

        logger.info("Full image path: {}", fullPathWithFileName);
        if(extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpeg")){

            logger.info("File extension: {}", extension);
            File folder = new File(path);
            if(!folder.exists()){
                folder.mkdirs(); // this will create the folder given in path
            }
            //upload file
            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
            return fileNameWithExtension;
        }
        else{
            throw new BadApiRequestException("File with extension - "+ extension+" not allowed!!");
        }


    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {

        String fullPath = path+File.separator+name;
        InputStream inputStream = new FileInputStream(fullPath);
        return inputStream;
    }
}
