package com.pyro.api;

import com.pyro.entities.DBFile;
import com.pyro.service.Toxls;
import com.pyro.service.UploadFileResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FileController {

    @Autowired
    Toxls toxls;

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private com.pyro.service.DBFileStorageService DBFileStorageService;

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        DBFile dbFile = DBFileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(String.valueOf(dbFile.getId()))
                .toUriString();

        return new UploadFileResponse(dbFile.getFileName(), fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    @PostMapping("/uploadMultipleFiles")
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> {
                    try {
                        return uploadFile(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/getFile/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) throws IOException {
        // Load file from database
        DBFile dbFile = DBFileStorageService.getFile(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dbFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
                .body(new ByteArrayResource(dbFile.getData()));
    }

    @GetMapping("/getPriceFile")
    public ResponseEntity<Resource> downloadXlsFile() throws IOException {
        // Load file from database
        File file = new File("1.xls");
        file.delete();
        toxls.writeIntoExcel("1.xls");
        toxls.writeIntoWord("1.docx");
        FileInputStream fis = new FileInputStream("1.xls");
        file = new File("1.xls");
        byte[] bytes = new byte[(int) file.length()];
        fis.read(bytes);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/xml"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"1.xls\"")
                .body(new ByteArrayResource(bytes));
    }
}
