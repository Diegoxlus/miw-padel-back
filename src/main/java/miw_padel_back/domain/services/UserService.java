package miw_padel_back.domain.services;

import miw_padel_back.infraestructure.api.dtos.UserLoginDto;
import miw_padel_back.infraestructure.api.dtos.TokenDto;
import miw_padel_back.domain.model.User;
import miw_padel_back.domain.persistence.UserPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    private final UserPersistence userPersistence;

    @Autowired
    public UserService(UserPersistence userPersistence) {
        this.userPersistence = userPersistence;
    }

    public Mono<User> create(User user) {
        return this.userPersistence.create(user);
    }

    public Mono<TokenDto> login(UserLoginDto userLoginDto) {
        return this.userPersistence.login(userLoginDto);
    }
}
