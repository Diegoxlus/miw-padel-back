package miw_padel_back.domain.persistence;

import miw_padel_back.domain.models.User;
import miw_padel_back.infraestructure.api.dtos.UserLoginDto;
import miw_padel_back.infraestructure.api.dtos.UserRegisterDto;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Repository
public interface UserPersistence {
    Mono<UserRegisterDto> create(UserRegisterDto userRegisterDto);

    Mono<User> findByEmail(String email);

    Mono<User> login(UserLoginDto userLoginDto);

    Mono<Void> saveImage(String email,byte[] bytes);

    Mono<byte[]> loadImage(String email) throws IOException;
}
