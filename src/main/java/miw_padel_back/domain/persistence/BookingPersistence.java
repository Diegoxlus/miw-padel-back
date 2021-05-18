package miw_padel_back.domain.persistence;

import miw_padel_back.domain.models.Booking;
import miw_padel_back.infraestructure.api.dtos.BookingDto;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.Date;

@Repository
public interface BookingPersistence {
    Flux<BookingDto> readByDate(LocalDate date);
    Flux<BookingDto> readBookingsByEmail(String email);
}
