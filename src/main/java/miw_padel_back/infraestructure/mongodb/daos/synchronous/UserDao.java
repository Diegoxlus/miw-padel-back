package miw_padel_back.infraestructure.mongodb.daos.synchronous;

import miw_padel_back.infraestructure.mongodb.entities.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends MongoRepository<UserEntity,String> {

}
