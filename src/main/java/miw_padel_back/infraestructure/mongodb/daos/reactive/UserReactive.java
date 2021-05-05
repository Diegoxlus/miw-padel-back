package miw_padel_back.infraestructure.mongodb.daos.reactive;

import miw_padel_back.infraestructure.mongodb.entities.UserEntity;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Mono;

public interface UserReactive extends ReactiveSortingRepository<UserEntity, String> {
    Mono<UserEntity> findFirstByEmail(String email);
}
