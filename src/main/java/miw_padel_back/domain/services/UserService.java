package miw_padel_back.domain.services;

import miw_padel_back.domain.model.AuthRequest;
import miw_padel_back.domain.model.TokenDto;
import miw_padel_back.domain.model.User;
import miw_padel_back.domain.persistence.UserPersistence;
import miw_padel_back.infraestructure.mongodb.entities.UserEntity;
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

    public Mono<UserEntity> create(User user) {
        return this.userPersistence.create(user);
    }

    public Mono<TokenDto> login(AuthRequest authRequest) {
        return this.userPersistence.login(authRequest);
    }
}
