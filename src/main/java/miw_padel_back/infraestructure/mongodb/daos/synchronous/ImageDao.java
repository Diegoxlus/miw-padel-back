package miw_padel_back.infraestructure.mongodb.daos.synchronous;

import miw_padel_back.infraestructure.mongodb.entities.ImageEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ImageDao extends MongoRepository<ImageEntity,String> {
}
