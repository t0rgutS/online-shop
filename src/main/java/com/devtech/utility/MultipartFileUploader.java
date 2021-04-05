package com.devtech.utility;

import com.devtech.exception.FileUploadException;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.MultipartConfigElement;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
public class MultipartFileUploader {
    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(5));
        factory.setMaxRequestSize(DataSize.ofMegabytes(10));
        return factory.createMultipartConfig();
    }

    public String uploadFile(MultipartFile file) {
        if (file != null)
            if (!file.isEmpty())
                try {
                    byte[] bytes = file.getBytes();
                    String filename = "../resources/static/images/" + file.getName();
                    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filename)));
                    stream.write(bytes);
                    stream.close();
                    return filename;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            else throw new FileUploadException("Файл " + file.getName() + " пуст!");
        else throw new FileUploadException("Файл не выбран!");
        return null;
    }
}
