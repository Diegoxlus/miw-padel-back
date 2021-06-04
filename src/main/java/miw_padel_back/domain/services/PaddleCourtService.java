package miw_padel_back.domain.services;

import miw_padel_back.domain.models.PaddleCourt;
import miw_padel_back.domain.persistence.PaddleCourtPersistence;
import miw_padel_back.infraestructure.api.dtos.PaddleCourtAvailabilityDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
public class PaddleCourtService {
    private final PaddleCourtPersistence paddleCourtPersistence;

    @Autowired
    public PaddleCourtService(PaddleCourtPersistence paddleCourtPersistence) {
        this.paddleCourtPersistence = paddleCourtPersistence;
    }

    public Mono<PaddleCourt> create(PaddleCourt paddleCourt) {
        return this.paddleCourtPersistence.create(paddleCourt);
    }

    public Flux<PaddleCourt> readAllOrderByName() {
        return this.paddleCourtPersistence.readAllOrderByName();
    }

    public Flux<PaddleCourtAvailabilityDto> readAvailabilityByDate(LocalDate date) {
        return this.paddleCourtPersistence.readAvailabilityByDate(date);
    }

    public Mono<Void> delete(String name) {
        return this.paddleCourtPersistence.deleteByName(name);
    }

    public Mono<PaddleCourt> edit(PaddleCourt paddleCourt) {
        return this.paddleCourtPersistence.edit(paddleCourt);
    }
}
