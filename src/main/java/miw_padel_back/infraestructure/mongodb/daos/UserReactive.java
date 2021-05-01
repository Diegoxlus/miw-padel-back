package miw_padel_back.infraestructure.mongodb.daos;

import miw_padel_back.infraestructure.mongodb.entities.UserEntity;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

@Configuration
public interface UserReactive extends ReactiveMongoRepository<UserEntity, String > {
}
