package miw_padel_back.infraestructure.mongodb.persistence;

import miw_padel_back.domain.exceptions.ConflictException;
import miw_padel_back.domain.exceptions.ForbiddenException;
import miw_padel_back.domain.exceptions.NotFoundException;
import miw_padel_back.domain.models.Booking;
import miw_padel_back.domain.persistence.BookingPersistence;
import miw_padel_back.infraestructure.api.dtos.BookingDto;
import miw_padel_back.infraestructure.mongodb.daos.reactive.BookingReactive;
import miw_padel_back.infraestructure.mongodb.daos.reactive.PaddleCourtReactive;
import miw_padel_back.infraestructure.mongodb.daos.reactive.UserReactive;
import miw_padel_back.infraestructure.mongodb.entities.BookingEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Repository
public class BookingPersistenceMDB implements BookingPersistence {

    private final BookingReactive bookingReactive;
    private final UserReactive userReactive;
    private final PaddleCourtReactive paddleCourtReactive;


    @Autowired
    public BookingPersistenceMDB(BookingReactive bookingReactive, UserReactive userReactive, PaddleCourtReactive paddleCourtReactive) {
        this.bookingReactive = bookingReactive;
        this.userReactive = userReactive;
        this.paddleCourtReactive = paddleCourtReactive;
    }

    @Override
    public Flux<BookingDto> readByDate(LocalDate date) {
        return this.bookingReactive.findAllByDate(date)
                .map(BookingEntity::toBookingDto);
    }

    @Override
    public Flux<BookingDto> readBookingsByEmail(String email) {
        return this.bookingReactive.findAllByOrderByDateAsc()
                .filter(bookingEntity -> bookingEntity.getUser().getEmail().equals(email))
                .map(BookingEntity::toBookingDto);
    }

    @Override
    public Flux<BookingDto> readAll() {
        return this.bookingReactive.findAllByOrderByDateAsc()
                .map(BookingEntity::toBookingDto);
    }

    @Override
    public Flux<BookingDto> readBookingsByEmailAndDate(String email, LocalDate date) {
        return this.bookingReactive.findAllByDate(date)
                .filter(bookingEntity -> bookingEntity.getUser().getEmail().equals(email))
                .map(BookingEntity::toBookingDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        return this.bookingReactive.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Booking with id:" + id + " not exist")))
                .flatMap(this.bookingReactive::delete);
    }

    @Override
    public Mono<Void> deleteMyBooking(String id,String email) {
        return this.bookingReactive.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Booking with id:" + id + " not exist")))
                .filter(bookingEntity -> bookingEntity.getUser().getEmail().equals(email))
                .switchIfEmpty(Mono.error(new ForbiddenException("Booking with id:" + id+ "is not yours")))
                .flatMap(this.bookingReactive::delete);

    }

    @Override
    public Mono<BookingDto> create(BookingDto bookingDto) {
        return this.bookingReactive.findAllByDate(bookingDto.getDate())
                .filter(bookingEntity -> bookingEntity.getTimeRange().equals(bookingDto.getTimeRange()) && bookingEntity.getPaddleCourt().getName().equals(bookingDto.getPaddleCourtName()))
                .flatMap(bookingEntity -> Mono.error(new ConflictException("Booking already exists in this range: "+ bookingDto.getTimeRange())))
                .then(this.userReactive.findFirstByEmail(bookingDto.getEmail()))
                .switchIfEmpty(Mono.error(new NotFoundException("User with email: "+bookingDto.getEmail()+ "not exists")))
                .flatMap(userEntity -> {
                    var bookingEntity = new BookingEntity(bookingDto);
                    bookingEntity.setUser(userEntity);
                    return paddleCourtReactive.findFirstByName(bookingDto.getPaddleCourtName())
                            .switchIfEmpty(Mono.error(new NotFoundException("Paddle court with name: "+bookingDto.getPaddleCourtName()+ "not exists")))
                            .flatMap(paddleCourtEntity -> {
                                bookingEntity.setPaddleCourt(paddleCourtEntity);
                                return this.bookingReactive.save(bookingEntity);
                            });

                })
                .map(BookingEntity::toBookingDto);
    }
}
