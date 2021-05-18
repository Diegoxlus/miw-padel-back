package miw_padel_back.infraestructure.mongodb.daos.reactive;

import miw_padel_back.domain.models.PaddleCourt;
import miw_padel_back.infraestructure.mongodb.entities.PaddleCourtEntity;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PaddleCourtReactive extends ReactiveSortingRepository<PaddleCourtEntity,String> {
    Mono<PaddleCourtEntity> readFirstByNameOrderByName(String name);
    Flux<PaddleCourtEntity> findAll();
}
