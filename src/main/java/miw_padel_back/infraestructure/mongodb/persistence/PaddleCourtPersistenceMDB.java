package miw_padel_back.infraestructure.mongodb.persistence;

import miw_padel_back.domain.exceptions.BadRequestException;
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
        if (Boolean.FALSE.equals(paddleCourt.checkTimes())) {
            return Mono.error(new BadRequestException("Check hours for this paddle court"));
        }
        return this.assertNameNotExists(paddleCourt.getName())
                .then(this.paddleCourtReactive.save(new PaddleCourtEntity(paddleCourt)))
                .map(PaddleCourtEntity::toPaddleCourt);
    }

    @Override
    public Mono<PaddleCourt> readByName(String name) {
        return this.assertNameExists(name)
                .map(PaddleCourtEntity::toPaddleCourt);
    }

    public Flux<PaddleCourt> readAllOrderByName() {
        return paddleCourtReactive.findAllByOrderByName()
                .map(PaddleCourtEntity::toPaddleCourt);
    }

    public Mono<PaddleCourtAvailabilityDto> readAvailabilityByNameAndDate(String name, LocalDate date) {
        return this.assertNameExists(name)
                .map(paddleCourtEntity -> {
                    var paddleCourtAvailabilityDto = paddleCourtEntity
                            .toPaddleCourt()
                            .createPaddleCourtAvailabilityDtoWithHours(date);
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
        return this.readAllOrderByName()
                .filter(paddleCourt -> !paddleCourt.isDisabled())
                .map(paddleCourt -> paddleCourt.createPaddleCourtAvailabilityDtoWithHours(date))
                .flatMap(paddleCourtAvailabilityDto -> this.bookingReactive
                        .findAllByDate(date)
                        .doOnNext(bookingEntity -> {
                            if (paddleCourtAvailabilityDto.getAvailabilityHours().containsKey(bookingEntity.getTimeRange()) &&
                                    bookingEntity.getPaddleCourt().getName().equals(paddleCourtAvailabilityDto.getName())) {
                                paddleCourtAvailabilityDto.getAvailabilityHours().put(bookingEntity.getTimeRange(), false);
                            }
                        }).thenMany(Flux.just(paddleCourtAvailabilityDto)))
                .distinct(PaddleCourtAvailabilityDto::getName);
    }


    public Mono<Void> assertNameNotExists(String name) {
        return this.paddleCourtReactive.readFirstByNameOrderByName(name)
                .flatMap(userEntity -> Mono.error(
                        new ConflictException("Name " + name + " already exists")
                ));
    }

    Mono<PaddleCourtEntity> assertNameExists(String name) {
        return this.paddleCourtReactive.readFirstByNameOrderByName(name)
                .switchIfEmpty(Mono.error(
                        new NotFoundException("Non exists paddle court with name " + name))
                );
    }

    Mono<PaddleCourtEntity> assertIdExists(String id) {
        return this.paddleCourtReactive.findById(id)
                .switchIfEmpty(Mono.error(
                        new NotFoundException("Non exists paddle court with this id"))
                );
    }

    public Mono<Void> deleteByName(String name) {
        return this.assertNameExists(name)
                .flatMap(paddleCourtEntity -> this.bookingReactive.findAll()
                        .filter(bookingEntity -> bookingEntity.getPaddleCourt().getName().equals(name))
                        .flatMap(this.bookingReactive::delete)
                        .then(this.paddleCourtReactive.findFirstByName(name))
                        .flatMap(this.paddleCourtReactive::delete));
    }

    @Override
    public Mono<PaddleCourt> edit(PaddleCourt paddleCourt) {
        return this.assertIdExists(paddleCourt.getId())
                .flatMap(paddleCourtEntity -> {
                    paddleCourtEntity = new PaddleCourtEntity(paddleCourt);
                    return paddleCourtReactive.save(paddleCourtEntity);
                })
                .map(PaddleCourtEntity::toPaddleCourt);
    }
}
