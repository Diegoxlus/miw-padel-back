package miw_padel_back.domain.persistence;

import miw_padel_back.domain.model.User;
import miw_padel_back.infraestructure.mongodb.entities.UserEntity;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserPersistence {
    Mono<UserEntity> create(User user);
    Mono<User> findByEmail(String email);
}
