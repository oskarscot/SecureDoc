package scot.oskar.securedoc.data.dto.file;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import scot.oskar.securedoc.data.model.Collection;
import scot.oskar.securedoc.data.model.File;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileResponseDTO {

  private UUID fileId;
  private String originalName;
  private String hashedName;
  private Collection collection;
  private UUID ownerId;
  private LocalDateTime createdAt;
  private LocalDateTime uploadedAt;

  public static FileResponseDTO fromFile(File file) {
    return new FileResponseDTO(
        file.getId(),
        file.getOriginalName(),
        file.getHashedName(),
        file.getCollection(),
        file.getOwner().getId(),
        file.getCreatedAt(),
        file.getUpdatedAt()
    );
  }

}
