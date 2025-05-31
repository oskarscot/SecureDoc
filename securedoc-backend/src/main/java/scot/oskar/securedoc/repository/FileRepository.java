package scot.oskar.securedoc.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import scot.oskar.securedoc.data.model.File;

public interface FileRepository extends JpaRepository<File, UUID> {

  List<File> findAllByOwnerId(UUID id);

}
