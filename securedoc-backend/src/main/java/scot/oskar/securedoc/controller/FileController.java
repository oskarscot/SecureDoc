package scot.oskar.securedoc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import scot.oskar.securedoc.service.FileService;

@RestController
@RequestMapping("api/v1/files")
public class FileController {

  private final FileService fileService;

  public FileController(FileService fileService) {
    this.fileService = fileService;
  }

  @PostMapping("/upload")
  public ResponseEntity<?> uploadFile() {
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }

  @PostMapping("/")
  public ResponseEntity<?> getAllFiles() {
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }

  @PostMapping("/{id}")
  public ResponseEntity<?> getFile(@PathVariable String id) {
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteFile(@PathVariable String id) {
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
  }
}
