package scot.oskar.securedoc.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import scot.oskar.securedoc.annotation.CurrentUser;
import scot.oskar.securedoc.data.ApiResponse;
import scot.oskar.securedoc.data.dto.file.FileResponseDTO;
import scot.oskar.securedoc.data.model.File;
import scot.oskar.securedoc.data.model.User;
import scot.oskar.securedoc.service.FileService;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

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

  @GetMapping()
  public ResponseEntity<?> getAllFiles(@CurrentUser User user) {
    List<File> allFilesForUser = fileService.getAllFilesForUser(user.getId());
    final ApiResponse response = ApiResponse.builder()
        .success(true)
        .message("Files retrieved successfully")
        .resource(allFilesForUser.stream().map(FileResponseDTO::fromFile).toList())
        .build();
    return ResponseEntity.ok(response);
  }

  @PostMapping("/{id}")
  public ResponseEntity<?> getFile(@PathVariable UUID id, @CurrentUser User user) {
    final File file = fileService.getFile(id);
    final ApiResponse response = ApiResponse.builder()
        .success(true)
        .message("File retrieved successfully")
        .resource(FileResponseDTO.fromFile(file))
        .build();
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteFile(@PathVariable UUID id, @CurrentUser User user) {
    fileService.deleteFile(user.getId(), id);
    final ApiResponse response = ApiResponse.builder()
        .success(true)
        .message("File deleted successfully")
        .build();
    return ResponseEntity.ok(response);
  }
}
