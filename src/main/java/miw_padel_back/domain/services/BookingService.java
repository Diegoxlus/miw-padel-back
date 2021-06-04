package miw_padel_back.domain.services;

import miw_padel_back.domain.persistence.BookingPersistence;
import miw_padel_back.infraestructure.api.dtos.BookingDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
public class BookingService {
    private final BookingPersistence bookingPersistence;

    public BookingService(BookingPersistence bookingPersistence) {
        this.bookingPersistence = bookingPersistence;
    }

    public Flux<BookingDto> readBookingsByDate(LocalDate date) {
        return this.bookingPersistence.readByDate(date);
    }

    public Flux<BookingDto> readAll() {
        return this.bookingPersistence.readAll();
    }

    public Flux<BookingDto> readBookingsByEmail(String email) {
        return this.bookingPersistence.readBookingsByEmail(email);
    }

    public Flux<BookingDto> readBookingsByEmailAndDate(String email, LocalDate date) {
        return this.bookingPersistence.readBookingsByEmailAndDate(email, date);
    }

    public Mono<Void> delete(String id) {
        return this.bookingPersistence.delete(id);
    }

    public Mono<Void> deleteMyBooking(String id, String playerEmail) {
        return this.bookingPersistence.deleteMyBooking(id, playerEmail);
    }

    public Mono<BookingDto> create(BookingDto bookingDto) {
        return this.bookingPersistence.create(bookingDto);
    }
}
