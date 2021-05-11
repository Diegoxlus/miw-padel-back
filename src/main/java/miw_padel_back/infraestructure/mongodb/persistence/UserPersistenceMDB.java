package miw_padel_back.infraestructure.mongodb.persistence;

import miw_padel_back.configuration.security.PBKDF2Encoder;
import miw_padel_back.domain.exceptions.ConflictException;
import miw_padel_back.domain.exceptions.NotFoundException;
import miw_padel_back.infraestructure.api.dtos.UserLoginDto;
import miw_padel_back.domain.models.User;
import miw_padel_back.domain.persistence.UserPersistence;
import miw_padel_back.infraestructure.api.dtos.UserRegisterDto;
import miw_padel_back.infraestructure.mongodb.daos.reactive.UserReactive;
import miw_padel_back.infraestructure.mongodb.entities.UserEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class UserPersistenceMDB implements UserPersistence {
    private final UserReactive userReactive;
    private final PBKDF2Encoder passwordEncoder;

    @Autowired
    public UserPersistenceMDB(UserReactive userReactive, PBKDF2Encoder passwordEncoder) {
        this.userReactive = userReactive;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<UserRegisterDto> create(UserRegisterDto userRegisterDto) {
        var userEntity = new UserEntity();
        BeanUtils.copyProperties(userRegisterDto, userEntity);
        userEntity.setPassword(this.passwordEncoder.encode(userEntity.getPassword()));
        return this.assertEmailNotExists(userRegisterDto.getEmail())
                .then(this.userReactive.save(userEntity))
                .map(UserEntity::toUserRegisterDtoWithoutPassword);
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return this.userReactive.findFirstByEmail(email)
                .switchIfEmpty(Mono.error(
                        new NotFoundException("Not existent email " + email)
                ))
                .map(UserEntity::toUser);
    }

    @Override
    public Mono<User> login(UserLoginDto userLoginDto) {
        return this.findByEmail(userLoginDto.getEmail())
                .flatMap(userDetails -> {
                    if (this.passwordEncoder.encode(userLoginDto.getPassword()).equals(userDetails.getPassword())) {
                        return Mono.just(userDetails);
                    } else {
                        return Mono.error(new ConflictException("Incorrect password"));
                    }
                });
    }

    public Mono<Void> assertEmailNotExists(String email) {
        return this.userReactive.findFirstByEmail(email)
                .flatMap(userEntity -> Mono.error(
                        new ConflictException("User email: " + email + " already exists")
                ));
    }

}
