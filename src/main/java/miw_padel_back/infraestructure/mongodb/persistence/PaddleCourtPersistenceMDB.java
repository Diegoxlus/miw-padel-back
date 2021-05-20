package miw_padel_back.infraestructure.mongodb.persistence;

import miw_padel_back.domain.exceptions.ConflictException;
import miw_padel_back.domain.exceptions.NotFoundException;
import miw_padel_back.domain.models.PaddleCourt;
import miw_padel_back.domain.persistence.PaddleCourtPersistence;
import miw_padel_back.infraestructure.api.dtos.PaddleCourtAvailabilityDto;
import miw_padel_back.infraestructure.mongodb.daos.reactive.BookingReactive;
import miw_padel_back.infraestructure.mongodb.daos.reactive.PaddleCourtReactive;
import miw_padel_back.infraestructure.mongodb.entities.PaddleCourtEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    public Mono<PaddleCourtAvailabilityDto> readAvailabilityByNameAndDate(String name, LocalDate date) {
        return this.assertNameExists(name)
                .map(paddleCourtEntity -> {
                    var paddleCourtAvailabilityDto = paddleCourtEntity
                            .toPaddleCourt()
                            .createPaddleCourtAvailabilityDtoWithHours();
                    paddleCourtAvailabilityDto.setDate(date);
                    return paddleCourtAvailabilityDto;
                })
                .flatMap(paddleCourtAvailabilityDto -> this.bookingReactive.findAllByDate(date)
                        .filter(bookingEntity -> bookingEntity.getPaddleCourt().getName().equals(name))
                        .doOnNext(y -> {
                            if (paddleCourtAvailabilityDto.getAvailabilityHours().containsKey(y.getTimeRange())) {
                                paddleCourtAvailabilityDto.getAvailabilityHours().put(y.getTimeRange(), false);
                            }
                        })
                        .then(Mono.just(paddleCourtAvailabilityDto))
                );
    }

    @Override
    public Flux<PaddleCourtAvailabilityDto> readAvailabilityByDate(LocalDate date) {
            return this.readAll()
                    .map(PaddleCourt::createPaddleCourtAvailabilityDtoWithHours)
                    .flatMap(paddleCourtAvailabilityDto -> {
                        return this.bookingReactive.findAllByDate(date)
                        .doOnNext(bookingEntity -> {
                            if(paddleCourtAvailabilityDto.getAvailabilityHours().containsKey(bookingEntity.getTimeRange()) && bookingEntity.getPaddleCourt().getName().equals(paddleCourtAvailabilityDto.getName())){
                                paddleCourtAvailabilityDto.getAvailabilityHours().put(bookingEntity.getTimeRange(),false);
                            }
                        }).map(x -> paddleCourtAvailabilityDto);
                    })
                    .distinct(PaddleCourtAvailabilityDto::getName);

    }


    public Mono<Void> assertNameNotExists(String name) {
        return this.paddleCourtReactive.readFirstByNameOrderByName(name)
                .flatMap(userEntity -> Mono.error(
                        new ConflictException("Name " + name + "already exists")
                ));
    }

    Mono<PaddleCourtEntity> assertNameExists(String name) {
        return this.paddleCourtReactive.readFirstByNameOrderByName(name)
                .switchIfEmpty(Mono.error(
                        new NotFoundException("Non existent paddle court with name: " + name))
                );
    }
}
