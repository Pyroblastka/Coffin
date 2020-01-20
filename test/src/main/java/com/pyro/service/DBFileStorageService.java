package com.pyro.service;

import com.pyro.entities.DBFile;
import com.pyro.repositories.DBFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;

@Service
public class DBFileStorageService {

    @Autowired
    private DBFileRepository dbFileRepository;
    @Transactional
    public DBFile storeFile(MultipartFile file) throws IOException {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new ArithmeticException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            DBFile dbFile = new DBFile(fileName, file.getContentType(), file.getBytes());
            //dbFile.setId(1L);
            return dbFileRepository.save(dbFile);
        } catch (IOException ex) {
            throw new IOException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
    @Transactional
    public DBFile getFile(Long id) throws IOException {
        return dbFileRepository.findById(id)
                .orElseThrow(() -> new IOException("File not found with id " + id));
    }
    @Transactional
    public DBFile getFile(String fileName) throws IOException {
        return dbFileRepository.findByFileName(fileName);
    }
}