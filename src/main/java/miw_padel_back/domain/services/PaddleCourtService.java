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

    public Flux<PaddleCourt> readAll() {
        return this.paddleCourtPersistence.readAllOrderByName();
    }

    public Flux<PaddleCourtAvailabilityDto> readAvailabilityByDate(LocalDate date) {
        return this.paddleCourtPersistence.readAvailabilityByDate(date);
    }
}
