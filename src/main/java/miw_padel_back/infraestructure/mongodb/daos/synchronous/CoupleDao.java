package miw_padel_back.infraestructure.mongodb.daos.synchronous;

import miw_padel_back.infraestructure.mongodb.entities.CoupleEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CoupleDao extends MongoRepository<CoupleEntity,String> {
}
