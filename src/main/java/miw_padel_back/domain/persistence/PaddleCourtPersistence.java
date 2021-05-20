package miw_padel_back.domain.persistence;

import miw_padel_back.domain.models.PaddleCourt;
import miw_padel_back.infraestructure.api.dtos.PaddleCourtAvailabilityDto;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Repository
public interface PaddleCourtPersistence {
    Mono<PaddleCourt> create(PaddleCourt paddleCourt);

    Mono<PaddleCourt> readByName(String name);

    Flux<PaddleCourt> readAllOrderByName();

    Flux<PaddleCourtAvailabilityDto> readAvailabilityByDate(LocalDate date);
}
