package scot.oskar.securedoc.data.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

@Data
@Entity
@Table(name = "files")
public class File {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  private String originalName;
  private String hashedName; // file name in S3

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private FileMetadata metadata; // maybe store metadata directly in s3

  @ManyToOne
  @JoinColumn(name = "collections_id", nullable = true)
  private Collection collection;

  @CreationTimestamp
  private LocalDateTime createdAt;
  @UpdateTimestamp
  private LocalDateTime updatedAt;

}
