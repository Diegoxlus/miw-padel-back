package miw_padel_back.domain.services;

import miw_padel_back.domain.persistence.BookingPersistence;
import miw_padel_back.infraestructure.api.dtos.BookingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@Service
public class BookingService {
    @Autowired
    private BookingPersistence bookingPersistence;

    public Flux<BookingDto> readBookingByDate(LocalDate date) {
        return this.bookingPersistence.readByDate(date);
    }

    public Flux<BookingDto> readBookingsByEmail(String email) {
        return this.bookingPersistence.readBookingsByEmail(email);
    }
}
