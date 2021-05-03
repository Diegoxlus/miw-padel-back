package miw_padel_back.infraestructure.mongodb.persistence;

import miw_padel_back.domain.exceptions.ConflictException;
import miw_padel_back.domain.model.User;
import miw_padel_back.domain.persistence.UserPersistence;
import miw_padel_back.infraestructure.mongodb.daos.reactive.UserReactive;
import miw_padel_back.infraestructure.mongodb.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.webjars.NotFoundException;
import reactor.core.publisher.Mono;

@Repository
public class UserPersistenceMDB implements UserPersistence {
    UserReactive userReactive;

    @Autowired
    public UserPersistenceMDB(UserReactive userReactive) {
        this.userReactive = userReactive;
    }

    @Override
    public Mono<UserEntity> create(User user) {
        UserEntity userEntity = new UserEntity(user);
        return this.assertEmailNotExists(user.getEmail())
                .then(this.userReactive.save(userEntity));
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return this.userReactive.findFirstByEmail(email)
                .switchIfEmpty(Mono.error(
                        new NotFoundException("Not existent email: "+ email)
                ))
                .map(UserEntity::toUser);
    }

    public Mono<Void> assertEmailNotExists(String email) {
        return this.userReactive.findFirstByEmail(email)
                .flatMap(userEntity -> Mono.error(
                        new ConflictException("User email: "+ email +" already exists")
                ));
    }

}
