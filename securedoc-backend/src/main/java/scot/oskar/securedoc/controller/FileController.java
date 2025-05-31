package scot.oskar.securedoc.controller;

import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import scot.oskar.securedoc.annotation.CurrentUser;
import scot.oskar.securedoc.data.ApiResponse;
import scot.oskar.securedoc.data.dto.file.FileResponseDTO;
import scot.oskar.securedoc.data.model.File;
import scot.oskar.securedoc.data.model.User;
import scot.oskar.securedoc.service.FileService;

@RestController
@RequestMapping("api/v1/files")
public class FileController {

  private final FileService fileService;

  public FileController(FileService fileService) {
    this.fileService = fileService;
  }

  @PostMapping("/upload")
  public ResponseEntity<?> uploadFile(@CurrentUser User user, @RequestParam("file") MultipartFile file) {
    final File uploadFile = fileService.uploadFile(user.getId(), file);
    final URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path("/api/v1/files/{id}")
        .buildAndExpand(uploadFile.getId()).toUri();
    final ApiResponse response = ApiResponse.builder()
        .success(true)
        .message("File uploaded successfully")
        .resource(FileResponseDTO.fromFile(uploadFile))
        .build();

    return ResponseEntity.created(uri).body(response);
  }

  @PostMapping("/")
  public ResponseEntity<?> getAllFiles(@CurrentUser User user) {
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }

  @PostMapping("/{id}")
  public ResponseEntity<?> getFile(@PathVariable String id, @CurrentUser User user) {
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteFile(@PathVariable String id, @CurrentUser User user) {
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }
}
