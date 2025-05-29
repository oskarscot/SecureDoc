package scot.oskar.securedoc.data.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
@Entity
@Table(name = "collections")
public class Collection {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  private String name;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "collection")
  private List<File> files = new ArrayList<>();

}
