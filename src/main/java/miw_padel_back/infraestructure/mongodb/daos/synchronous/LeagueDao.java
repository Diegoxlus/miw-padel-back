package miw_padel_back.infraestructure.mongodb.daos.synchronous;

import miw_padel_back.infraestructure.mongodb.entities.LeagueEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LeagueDao extends MongoRepository<LeagueEntity,String> {
}
