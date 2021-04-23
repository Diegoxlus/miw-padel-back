package miw_padel_back.infraestructure.mongodb.daos;

import miw_padel_back.infraestructure.mongodb.entities.UserEntity;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;

public interface UserReactive extends ReactiveSortingRepository<UserEntity, String > {

}
