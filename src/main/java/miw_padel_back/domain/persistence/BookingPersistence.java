package miw_padel_back.domain.persistence;

import miw_padel_back.domain.models.Booking;
import miw_padel_back.infraestructure.api.dtos.BookingDto;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Repository
public interface BookingPersistence {
    Flux<BookingDto> readByDate(LocalDate date);
    Flux<BookingDto> readBookingsByEmail(String email);
    Flux<BookingDto> readAll();
    Flux<BookingDto> readBookingsByEmailAndDate(String email, LocalDate date);
    Mono<Void> delete(String id);
    Mono<Void> deleteMyBooking(String id,String playerEmail);
    Mono<Booking> create(BookingDto bookingDto);
}
