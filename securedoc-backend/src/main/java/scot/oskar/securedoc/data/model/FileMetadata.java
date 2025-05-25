package scot.oskar.securedoc.data.model;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileMetadata {

  private String fileName;
  private String fileExtension;
  private long size;
  private String checksum;
  private Map<String, Object> properties;

}
