package miw_padel_back.infraestructure.mongodb.persistence;

import miw_padel_back.domain.exceptions.ConflictException;
import miw_padel_back.domain.exceptions.NotFoundException;
import miw_padel_back.domain.models.PaddleCourt;
import miw_padel_back.domain.persistence.PaddleCourtPersistence;
import miw_padel_back.infraestructure.api.dtos.PaddleCourtAvailabilityDto;
import miw_padel_back.infraestructure.mongodb.daos.reactive.BookingReactive;
import miw_padel_back.infraestructure.mongodb.daos.reactive.PaddleCourtReactive;
import miw_padel_back.infraestructure.mongodb.entities.BookingEntity;
import miw_padel_back.infraestructure.mongodb.entities.PaddleCourtEntity;
import miw_padel_back.infraestructure.mongodb.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Repository
public class PaddleCourtPersistenceMDB implements PaddleCourtPersistence {

    private final PaddleCourtReactive paddleCourtReactive;
    private final BookingReactive bookingReactive;

    @Autowired
    public PaddleCourtPersistenceMDB(PaddleCourtReactive paddleCourtReactive, BookingReactive bookingReactive) {
        this.paddleCourtReactive = paddleCourtReactive;
        this.bookingReactive = bookingReactive;
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

    @Override
    public Flux<PaddleCourt> readAll() {
         return paddleCourtReactive.findAll()
                .map(PaddleCourtEntity::toPaddleCourt);
    }

    @Override
    public Mono<PaddleCourtAvailabilityDto> readAvailabilityByNameAndDate(String name, Date date) {
        return this.paddleCourtReactive.readFirstByName(name)
                .map(paddleCourtEntity -> {
                    var paddleCourtAvailabilityDto = paddleCourtEntity
                            .toPaddleCourt()
                            .createPaddleCourtAvailabilityDtoWithHours();
                    paddleCourtAvailabilityDto.setDate(date);
                    return paddleCourtAvailabilityDto;
                })
                .flatMap(paddleCourtAvailabilityDto-> this.bookingReactive.findAllByDate(date)
                        .filter(bookingEntity -> bookingEntity.getPaddleCourt().getName().equals(name))
                        .doOnNext(y->{
                            if (paddleCourtAvailabilityDto.getAvailabilityHours().containsKey(y.getTimeRange())) {
                                paddleCourtAvailabilityDto.getAvailabilityHours().put(y.getTimeRange(), false);
                            }
                        })
                        .then(Mono.just(paddleCourtAvailabilityDto))
                );
/*
        return this.bookingReactive.findAllByDate(date)
                .filter(x -> x.getPaddleCourt().getName().equals(name))
                .doOnNext(y -> {
                    if (paddleCourtAvailabilityDtoF.getAvailabilityHours().containsKey(y.getTimeRange())) {
                        paddleCourtAvailabilityDtoF.getAvailabilityHours().put(y.getTimeRange(), false);
                    }
                })
                .then(Mono.just(paddleCourtAvailabilityDtoF));*/
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
