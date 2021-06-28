package miw_padel_back.infraestructure.mongodb.persistence;

import miw_padel_back.domain.exceptions.ConflictException;
import miw_padel_back.domain.exceptions.NotFoundException;
import miw_padel_back.domain.models.CoupleState;
import miw_padel_back.domain.models.Gender;
import miw_padel_back.domain.persistence.CouplePersistence;
import miw_padel_back.infraestructure.api.dtos.CoupleDto;
import miw_padel_back.infraestructure.api.dtos.EmailDto;
import miw_padel_back.infraestructure.api.dtos.IdDto;
import miw_padel_back.infraestructure.mongodb.daos.reactive.CoupleReactive;
import miw_padel_back.infraestructure.mongodb.daos.reactive.UserReactive;
import miw_padel_back.infraestructure.mongodb.entities.CoupleEntity;
import miw_padel_back.infraestructure.mongodb.entities.UserEntity;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Repository
public class CouplePersistenceMDB implements CouplePersistence {

    private final CoupleReactive coupleReactive;
    private final UserReactive userReactive;

    public CouplePersistenceMDB(CoupleReactive coupleReactive, UserReactive userReactive) {
        this.userReactive = userReactive;
        this.coupleReactive = coupleReactive;
    }

    @Override
    public Flux<CoupleDto> readPlayerCouples(String email) {
        return this.userReactive.findFirstByEmail(email)
                .switchIfEmpty(Mono.error(new NotFoundException("User with email: " + email + "not exists")))
                .thenMany(this.coupleReactive.findAll())
                .filter(coupleEntity -> coupleEntity.getCaptain().getEmail().equals(email) || coupleEntity.getPlayer().getEmail().equals(email))
                .map(CoupleEntity::toCoupleDto);
    }

    @Override
    public Mono<CoupleDto> createCouplePetition(String emailCaptain, EmailDto emailDto) {
        if(emailCaptain.equals(emailDto.getEmail())){
            return Mono.error(new ConflictException("This email is yours"));
        }
        return this.assertCoupleNotExists(emailCaptain, emailDto.getEmail())
                .then(this.findUserByEmail(emailCaptain))
                .flatMap(captainEntity -> {
                    var coupleEntity = CoupleEntity.builder().captain(captainEntity).gender(captainEntity.getGender()).coupleState(CoupleState.PENDING).build();
                    return this.findUserByEmail(emailDto.getEmail())
                            .flatMap(playerEntity -> {
                                coupleEntity.setPlayer(playerEntity);
                                coupleEntity.setCreationDate(LocalDate.now());
                                if (playerEntity.getGender() != coupleEntity.getGender()) {
                                    coupleEntity.setGender(Gender.MIXED);
                                }
                                return this.coupleReactive.save(coupleEntity).map(CoupleEntity::toCoupleDto);
                            });
                });
    }

    @Override
    public Mono<CoupleDto> acceptCouplePetition(String playerEmail, IdDto idDto) {
        return this.findById(idDto.getId())
                .flatMap(coupleEntity -> {
                    if (coupleEntity.getPlayer().getEmail().equals(playerEmail)) {
                        coupleEntity.setCoupleState(CoupleState.CONSOLIDATED);
                        coupleEntity.setCreationDate(LocalDate.now());
                    } else {
                        return Mono.error(new ConflictException("This couple petition isn´t yours"));
                    }
                    return this.coupleReactive.save(coupleEntity).map(CoupleEntity::toCoupleDto);
                });

    }

    @Override
    public Mono<Void> deleteCouplePetition(String playerEmail, String id) {
        return this.findById(id)
                .flatMap(coupleEntity -> {
                    if (!coupleEntity.getPlayer().getEmail().equals(playerEmail)) {
                        return Mono.error(new ConflictException("This couple petition isn´t yours"));
                    } else if (!coupleEntity.getCoupleState().equals(CoupleState.PENDING)) {
                        return Mono.error(new ConflictException("This couple state isn´t pending"));
                    } else {
                        return this.coupleReactive.delete(coupleEntity);
                    }
                });
    }

    public Mono<CoupleEntity> findById(String id) {
        return this.coupleReactive.findById(id)
                .switchIfEmpty(Mono.error(
                        new NotFoundException("Not exists couple with this id")
                ));
    }

    public Mono<UserEntity> findUserByEmail(String email) {
        return this.userReactive.findFirstByEmail(email)
                .switchIfEmpty(Mono.error(
                        new NotFoundException("Not existent email " + email)
                ));
    }

    public Mono<Void> assertCoupleNotExists(String captainEmail, String playerEmail) {
        return this.coupleReactive.findAll()
                .filter(coupleEntity -> coupleEntity.getCaptain().getEmail().equals(captainEmail) &&
                        coupleEntity.getPlayer().getEmail().equals(playerEmail) ||
                        coupleEntity.getCaptain().getEmail().equals(playerEmail) &&
                                coupleEntity.getPlayer().getEmail().equals(captainEmail)
                )
                .singleOrEmpty()
                .flatMap(coupleEntity -> {
                    if(coupleEntity.getCoupleState().equals(CoupleState.PENDING)){
                       return Mono.error(new ConflictException("Couple request already exists"));
                    }
                    else return Mono.error(new ConflictException("Couple already exists"));
                });
    }
}
