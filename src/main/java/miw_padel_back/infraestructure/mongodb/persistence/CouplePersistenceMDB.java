package miw_padel_back.infraestructure.mongodb.persistence;

import miw_padel_back.domain.exceptions.ConflictException;
import miw_padel_back.domain.exceptions.NotFoundException;
import miw_padel_back.domain.models.Couple;
import miw_padel_back.domain.models.CoupleState;
import miw_padel_back.domain.models.Gender;
import miw_padel_back.domain.persistence.CouplePersistence;
import miw_padel_back.infraestructure.api.dtos.EmailDto;
import miw_padel_back.infraestructure.api.dtos.IdDto;
import miw_padel_back.infraestructure.mongodb.daos.reactive.CoupleReactive;
import miw_padel_back.infraestructure.mongodb.daos.reactive.UserReactive;
import miw_padel_back.infraestructure.mongodb.entities.CoupleEntity;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class CouplePersistenceMDB implements CouplePersistence {

    private final CoupleReactive coupleReactive;
    private final UserReactive userReactive;

    public CouplePersistenceMDB(CoupleReactive coupleReactive, UserReactive userReactive) {
        this.userReactive = userReactive;
        this.coupleReactive = coupleReactive;
    }

    @Override
    public Flux<Couple> readPlayerCouples(String email) {
        return this.userReactive.findFirstByEmail(email)
                .switchIfEmpty(Mono.error(new NotFoundException("User with email: "+email+ "not exists")))
                .thenMany(this.coupleReactive.findAll())
                .filter(coupleEntity -> coupleEntity.getCaptain().getEmail().equals(email) || coupleEntity.getPlayer().getEmail().equals(email))
                .map(CoupleEntity::toCouple);
    }

    @Override
    public Mono<Couple> createCouplePetition(String emailCaptain, EmailDto emailDto) {
        return this.userReactive.findFirstByEmail(emailCaptain)
                .flatMap(captainEntity ->{
                    var coupleEntity = CoupleEntity.builder().captain(captainEntity).gender(captainEntity.getGender()).coupleState(CoupleState.PENDING).build();
                    return this.userReactive.findFirstByEmail(emailDto.getEmail())
                            .flatMap(playerEntity -> {
                                coupleEntity.setPlayer(playerEntity);
                                if(playerEntity.getGender()!=coupleEntity.getGender()){
                                    coupleEntity.setGender(Gender.MIXED);
                                }
                                return this.coupleReactive.save(coupleEntity).map(CoupleEntity::toCouple);
                    });
                });
    }

    @Override
    public Mono<Couple> acceptCouplePetition(String playerEmail, IdDto idDto) {
        return this.findById(idDto.getId())
                .flatMap(coupleEntity -> {
                    if(coupleEntity.getPlayer().getEmail().equals(playerEmail)){
                        coupleEntity.setCoupleState(CoupleState.CONSOLIDATED);
                    }
                    else{
                        return Mono.error(new ConflictException("This couple petition isn´t yours"));
                    }
                    return this.coupleReactive.save(coupleEntity).map(CoupleEntity::toCouple);
                });

    }

    public Mono<CoupleEntity> findById(String id) {
        return this.coupleReactive.findById(id)
                .switchIfEmpty(Mono.error(
                        new NotFoundException("Not exists couple with this id" )
                ));
    }
}
