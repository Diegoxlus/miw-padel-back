package miw_padel_back.infraestructure.mongodb.persistence;

import miw_padel_back.domain.exceptions.NotFoundException;
import miw_padel_back.domain.models.Couple;
import miw_padel_back.domain.persistence.CouplePersistence;
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
}
