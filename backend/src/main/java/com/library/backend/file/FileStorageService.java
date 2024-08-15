package com.library.backend.file;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
@Slf4j // for log
public class FileStorageService {

    @Value("${application.file.upload.photos.output-path}")
    private String fileUploadPath;

    public String saveFile(
            @Nonnull MultipartFile file,
            @Nonnull Integer userId
            ){
        final String fileUploadSubPath = "users" + File.separator /*be it a "/" or " " or "-" .. */ + userId;
        return uploadFile(file, fileUploadSubPath);
    }

    private String uploadFile(@Nonnull MultipartFile file, @Nonnull String subPath) {
        final String finalUploadPath = fileUploadPath + File.separator + subPath;
        File targetFolder = new File(finalUploadPath);
        if(!targetFolder.exists()) {
            boolean success = targetFolder.mkdirs(); // with s to create folder and all subfolders
            if(!success) {
                log.warn("Failed to create directory: {}", targetFolder.getAbsolutePath());
                return null;
            }
        }
        final String fileExtension = getFileExtension(file.getOriginalFilename());
        String targetFilePath = finalUploadPath + File.separator + System.currentTimeMillis() + "." + fileExtension; // ./uploads/users/1/645684656151.jpg
        Path targetPath = Paths.get(targetFilePath);
        try {
            Files.write(targetPath, file.getBytes());
            log.info("File saved: {}", targetFilePath);
            return targetFilePath;
        }catch(IOException e) {
            log.error("Failed to write file: {}", targetFilePath, e);
        }
        return null;
    }

    private String getFileExtension(String fileName) {
        if(fileName == null || fileName.isEmpty()) {
            return "";
        }
        int lastDotIndex = fileName.lastIndexOf(".");
        if(lastDotIndex == -1) {
            return "";
        }
        return fileName.substring(lastDotIndex +1).toLowerCase();
    }
}
