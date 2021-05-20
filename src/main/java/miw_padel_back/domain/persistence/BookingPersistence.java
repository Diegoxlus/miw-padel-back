package miw_padel_back.domain.persistence;

import miw_padel_back.infraestructure.api.dtos.BookingDto;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@Repository
public interface BookingPersistence {
    Flux<BookingDto> readByDate(LocalDate date);
    Flux<BookingDto> readBookingsByEmail(String email);
    Flux<BookingDto> readAll();
    Flux<BookingDto> readBookingsByEmailAndDate(String email, LocalDate date);
}
