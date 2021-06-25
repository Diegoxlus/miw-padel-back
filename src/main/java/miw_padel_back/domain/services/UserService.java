package miw_padel_back.domain.services;

import miw_padel_back.configuration.security.JWTUtil;
import miw_padel_back.domain.persistence.UserPersistence;
import miw_padel_back.infraestructure.api.dtos.TokenDto;
import miw_padel_back.infraestructure.api.dtos.UserLoginDto;
import miw_padel_back.infraestructure.api.dtos.UserRegisterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    private final UserPersistence userPersistence;
    private final JWTUtil jwtUtil;

    @Autowired
    public UserService(UserPersistence userPersistence, JWTUtil jwtUtil) {
        this.userPersistence = userPersistence;
        this.jwtUtil = jwtUtil;
    }

    public Mono<UserRegisterDto> create(UserRegisterDto userRegisterDto) {
        return this.userPersistence.create(userRegisterDto);
    }

    public Mono<TokenDto> login(UserLoginDto userLoginDto) {
        return this.userPersistence.login(userLoginDto)
                .flatMap(user -> Mono.just(new TokenDto(this.jwtUtil.generateToken(user)))
                );
    }

    public Mono<Void> saveImage(String email, byte[] bytes) {
        return this.userPersistence.saveImage(email, bytes);
    }

    public Mono<byte[]> loadImage(String email) {
        return this.userPersistence.loadImage(email);
    }
}
