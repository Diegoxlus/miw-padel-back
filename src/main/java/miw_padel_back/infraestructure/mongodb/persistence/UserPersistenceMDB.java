package miw_padel_back.infraestructure.mongodb.persistence;

import miw_padel_back.domain.model.User;
import miw_padel_back.domain.persistence.UserPersistence;
import miw_padel_back.infraestructure.mongodb.daos.reactive.UserReactive;
import miw_padel_back.infraestructure.mongodb.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
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
        return this.userReactive
                .save(userEntity);
    }
}
