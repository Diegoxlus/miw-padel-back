package miw_padel_back.infraestructure.mongodb.daos;

import miw_padel_back.infraestructure.mongodb.entities.UserEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
public interface UserReactive extends ReactiveMongoRepository<UserEntity, String > {
}
