package miw_padel_back.infraestructure.mongodb.persistence;

import miw_padel_back.configuration.security.JWTUtil;
import miw_padel_back.configuration.security.PBKDF2Encoder;
import miw_padel_back.domain.exceptions.ConflictException;
import miw_padel_back.domain.exceptions.NotFoundException;
import miw_padel_back.infraestructure.api.dtos.UserLoginDto;
import miw_padel_back.infraestructure.api.dtos.TokenDto;
import miw_padel_back.domain.model.User;
import miw_padel_back.domain.persistence.UserPersistence;
import miw_padel_back.infraestructure.mongodb.daos.reactive.UserReactive;
import miw_padel_back.infraestructure.mongodb.entities.UserEntity;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class UserPersistenceMDB implements UserPersistence {
    private final UserReactive userReactive;
    private final PBKDF2Encoder passwordEncoder;
    private final JWTUtil jwtUtil;

    @Autowired
    public UserPersistenceMDB(UserReactive userReactive, PBKDF2Encoder passwordEncoder, JWTUtil jwtUtil) {
        this.userReactive = userReactive;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<User> create(User user) {
        UserEntity userEntity;
        try{
             userEntity = new UserEntity(user);
        } catch (FatalBeanException fatalBeanException){
            return Mono.error(new ConflictException("Empty fields"));
        }
        userEntity.setPassword(this.passwordEncoder.encode(userEntity.getPassword()));
        return this.assertEmailNotExists(user.getEmail())
                .then(this.userReactive.save(userEntity))
                .map(UserEntity::toUserWithoutPassword);
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
    public Mono<TokenDto> login(UserLoginDto userLoginDto) {
        return this.findByEmail(userLoginDto.getEmail()).flatMap((userDetails) -> {
            if (this.passwordEncoder.encode(userLoginDto.getPassword()).equals(userDetails.getPassword())) {
                return Mono.just(new TokenDto(this.jwtUtil.generateToken(userDetails)));
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
