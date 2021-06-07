package miw_padel_back.infraestructure.mongodb.persistence;

import miw_padel_back.configuration.security.PBKDF2Encoder;
import miw_padel_back.domain.exceptions.ConflictException;
import miw_padel_back.domain.exceptions.NotFoundException;
import miw_padel_back.domain.models.User;
import miw_padel_back.domain.persistence.UserPersistence;
import miw_padel_back.infraestructure.api.dtos.UserLoginDto;
import miw_padel_back.infraestructure.api.dtos.UserRegisterDto;
import miw_padel_back.infraestructure.mongodb.daos.reactive.ImageReactive;
import miw_padel_back.infraestructure.mongodb.daos.reactive.UserReactive;
import miw_padel_back.infraestructure.mongodb.entities.ImageEntity;
import miw_padel_back.infraestructure.mongodb.entities.UserEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Repository
public class UserPersistenceMDB implements UserPersistence {
    private final UserReactive userReactive;
    private final PBKDF2Encoder passwordEncoder;
    private final ImageReactive imageReactive;

    @Autowired
    public UserPersistenceMDB(UserReactive userReactive, PBKDF2Encoder passwordEncoder, ImageReactive imageReactive) {
        this.userReactive = userReactive;
        this.passwordEncoder = passwordEncoder;
        this.imageReactive = imageReactive;
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

    @Override
    public Mono<Void> saveImage(String email,byte[] bytes) {
        return this.userReactive.findFirstByEmail(email)
                .flatMap(userEntity -> {
                    var imageEntity = ImageEntity.builder().userEntity(userEntity).imageBytes(bytes).build();
                    return this.imageReactive.findAll()
                            .switchIfEmpty(this.imageReactive.save(imageEntity))
                            .filter(imageEntity1 -> imageEntity1.getUserEntity().getEmail().equals(email))
                            .switchIfEmpty(this.imageReactive.save(imageEntity))
                            .flatMap(imageEntity2 -> {
                                imageEntity2.setImageBytes(bytes);
                                return this.imageReactive.save(imageEntity2);
                            }).then();
                });
    }

    @Override
    public Mono<byte[]> loadImage(String email) throws IOException {
        System.out.println(email);
        return this.imageReactive.findAll().filter(imageEntity -> imageEntity.getUserEntity().getEmail().equals(email))
                .switchIfEmpty(Mono.error(new NotFoundException("Not found image for "+ email)))
                .map(ImageEntity::getImageBytes)
                .single();
    }

    public Mono<Void> assertEmailNotExists(String email) {
        return this.userReactive.findFirstByEmail(email)
                .flatMap(userEntity -> Mono.error(
                        new ConflictException("User email: " + email + " already exists")
                ));
    }

}
