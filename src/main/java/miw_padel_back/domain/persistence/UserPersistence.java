package miw_padel_back.domain.persistence;

import miw_padel_back.infraestructure.api.dtos.UserLoginDto;
import miw_padel_back.infraestructure.api.dtos.TokenDto;
import miw_padel_back.domain.models.User;
import miw_padel_back.infraestructure.api.dtos.UserRegisterDto;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserPersistence {
    Mono<UserRegisterDto> create(UserRegisterDto userRegisterDto);

    Mono<User> findByEmail(String email);

    Mono<User> login(UserLoginDto userLoginDto);
}
