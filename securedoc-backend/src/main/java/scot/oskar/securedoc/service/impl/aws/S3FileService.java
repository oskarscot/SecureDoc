package scot.oskar.securedoc.service.impl.aws;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;
import scot.oskar.securedoc.data.model.File;
import scot.oskar.securedoc.data.model.User;
import scot.oskar.securedoc.exception.FileStorageException;
import scot.oskar.securedoc.exception.ResourceNotFoundException;
import scot.oskar.securedoc.repository.FileRepository;
import scot.oskar.securedoc.repository.UserRepository;
import scot.oskar.securedoc.service.FileService;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3FileService implements FileService {

  private final FileRepository fileRepository;
  private final UserRepository userRepository;
  private final S3Client s3Client;

  @Value("${app.aws.s3.bucket-name}")
  private String bucketName;

  @Value("${app.aws.cloudfront.domain}")
  private String cloudfrontDomain;

  public S3FileService(FileRepository fileRepository, UserRepository userRepository, S3Client s3Client) {
    this.fileRepository = fileRepository;
    this.userRepository = userRepository;
    this.s3Client = s3Client;
  }

  @Override
  public File uploadFile(UUID ownerId, MultipartFile file) {
    final String originalFilename = file.getOriginalFilename();
    final String hashName = DigestUtils.md5DigestAsHex(originalFilename.getBytes());
    final String extension = getFileExtension(originalFilename);
    final String key = String.format("%s/%s.%s", ownerId, hashName, extension);

    final User user = userRepository.findById(ownerId)
        .orElseThrow(() -> new ResourceNotFoundException("User", "id", ownerId));

    final File dbFile = File.builder()
        .originalName(originalFilename)
        .hashedName(hashName)
        .owner(user)
        .build();

    Map<String, String> metadata = new HashMap<>();
    metadata.put("Content-Type", file.getContentType());
    metadata.put("Content-Length", String.valueOf(file.getSize()));

    PutObjectRequest request = PutObjectRequest.builder()
        .bucket(bucketName)
        .key(key)
        .contentType(file.getContentType())
        .metadata(metadata)
        .build();

    try {
      s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
      fileRepository.save(dbFile);
    } catch (IOException e) {
      throw new FileStorageException("Unable to store file into S3: " + e.getMessage());
    }
    return dbFile;
  }

  @Override
  public void deleteFile(UUID ownerId, UUID fileId) {
    final File file = fileRepository.findById(fileId).orElseThrow(() -> new ResourceNotFoundException("File", "id", fileId));
    final String key = String.format("%s/%s.%s", ownerId, file.getHashedName(), getFileExtension(file.getHashedName()));

    DeleteObjectRequest request = DeleteObjectRequest.builder()
        .bucket(bucketName)
        .key(key)
        .build();

    s3Client.deleteObject(request);
    fileRepository.delete(file);
  }

  @Override
  public List<File> getAllFilesForUser(UUID ownerId) {
    final List<File> files = fileRepository.findAllByOwnerId(ownerId);
    return List.copyOf(files);
  }

  @Override
  public File getFile(UUID fileId) {
    return fileRepository.findById(fileId)
        .orElseThrow(() -> new ResourceNotFoundException("File", "id", fileId));
  }

  private String getFileExtension(String filename) {
    if (filename == null) {
      return "";
    }
    int dotIndex = filename.lastIndexOf('.');
    if (dotIndex < 0) {
      return "";
    }
    return filename.substring(dotIndex);
  }
}
