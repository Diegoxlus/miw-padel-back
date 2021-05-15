package miw_padel_back.infraestructure.mongodb.daos.synchronous;

import miw_padel_back.infraestructure.mongodb.entities.PaddleCourtEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaddleCourtDao extends MongoRepository<PaddleCourtEntity,String> {
}
