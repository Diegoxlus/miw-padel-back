package miw_padel_back.domain.persistence;

import miw_padel_back.domain.models.PaddleCourt;
import reactor.core.publisher.Mono;

public interface PaddleCourtPersistence {
    Mono<PaddleCourt> create (PaddleCourt paddleCourt);
    Mono<PaddleCourt> readByName(String name);
}
