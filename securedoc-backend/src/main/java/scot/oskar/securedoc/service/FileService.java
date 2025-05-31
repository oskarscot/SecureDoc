package scot.oskar.securedoc.service;

import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;
import scot.oskar.securedoc.data.model.File;

public interface FileService {

  File uploadFile(UUID ownerId, MultipartFile file);

  void deleteFile(UUID ownerId, UUID fileId);

  List<File> getAllFilesForUser(UUID ownerId);

  File getFile(UUID id);

}
