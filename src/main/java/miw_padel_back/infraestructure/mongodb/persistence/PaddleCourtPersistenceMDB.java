package miw_padel_back.infraestructure.mongodb.persistence;

import miw_padel_back.domain.exceptions.ConflictException;
import miw_padel_back.domain.exceptions.NotFoundException;
import miw_padel_back.domain.models.PaddleCourt;
import miw_padel_back.domain.persistence.PaddleCourtPersistence;
import miw_padel_back.infraestructure.mongodb.daos.reactive.PaddleCourtReactive;
import miw_padel_back.infraestructure.mongodb.daos.reactive.UserReactive;
import miw_padel_back.infraestructure.mongodb.entities.PaddleCourtEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class PaddleCourtPersistenceMDB implements PaddleCourtPersistence {

    private final PaddleCourtReactive paddleCourtReactive;

    @Autowired
    public PaddleCourtPersistenceMDB(PaddleCourtReactive paddleCourtReactive) {
        this.paddleCourtReactive = paddleCourtReactive;
    }

    public Mono<PaddleCourt> create(PaddleCourt paddleCourt) {
        /*return this.assertNameNotExists(paddleCourt.getName())
                .then(this.paddleCourtReactive.readFirstByName(paddleCourt.getName()))*/
    return null;
    }

    @Override
    public Mono<PaddleCourt> readByName(String name) {
        return null;
    }

    public Mono<Void> assertNameNotExists(String name) {
        return this.paddleCourtReactive.readFirstByName(name)
                .flatMap(userEntity -> Mono.error(
                        new ConflictException("Name "+name+ "already exists")
                ));
    }

    Mono<PaddleCourtEntity> assertNameExists(String name) {
        return this.paddleCourtReactive.readFirstByName(name)
                .switchIfEmpty(Mono.error(
                        new NotFoundException("Non existent paddle court with name: " + name))
                );
    }
}
