package miw_padel_back.infraestructure.mongodb.persistence;

import miw_padel_back.domain.models.Booking;
import miw_padel_back.domain.persistence.BookingPersistence;
import miw_padel_back.infraestructure.api.dtos.BookingDto;
import miw_padel_back.infraestructure.mongodb.daos.reactive.BookingReactive;
import miw_padel_back.infraestructure.mongodb.entities.BookingEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.Date;

@Repository
public class BookingPersistenceMDB implements BookingPersistence {

    private final BookingReactive bookingReactive;

    @Autowired
    public BookingPersistenceMDB(BookingReactive bookingReactive) {
        this.bookingReactive = bookingReactive;
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
}
