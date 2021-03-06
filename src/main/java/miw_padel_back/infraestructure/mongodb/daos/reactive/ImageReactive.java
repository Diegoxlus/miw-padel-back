package miw_padel_back.infraestructure.mongodb.daos.reactive;

import miw_padel_back.infraestructure.mongodb.entities.ImageEntity;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;

public interface ImageReactive extends ReactiveSortingRepository<ImageEntity, String> {
}
