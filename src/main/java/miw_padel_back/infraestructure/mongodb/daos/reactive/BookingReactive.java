package miw_padel_back.infraestructure.mongodb.daos.reactive;

import miw_padel_back.infraestructure.mongodb.entities.BookingEntity;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

public interface BookingReactive extends ReactiveSortingRepository<BookingEntity, String> {
    Flux<BookingEntity> findAllByDate(LocalDate date);

    Flux<BookingEntity> findAllByOrderByDateAsc();

}
