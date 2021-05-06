package miw_padel_back.domain.persistence;

import miw_padel_back.infraestructure.api.dtos.UserLoginDto;
import miw_padel_back.infraestructure.api.dtos.TokenDto;
import miw_padel_back.domain.models.User;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserPersistence {
    Mono<User> create(User user);

    Mono<User> findByEmail(String email);

    Mono<TokenDto> login(UserLoginDto userLoginDto);
}
