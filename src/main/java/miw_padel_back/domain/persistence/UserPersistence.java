package miw_padel_back.domain.persistence;

import miw_padel_back.domain.model.User;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserPersistence {
    Mono<User> create(User user);
}
