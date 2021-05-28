package miw_padel_back.infraestructure.mongodb.daos.reactive;

import miw_padel_back.infraestructure.mongodb.entities.CoupleEntity;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;

public interface CoupleReactive extends ReactiveSortingRepository<CoupleEntity,String> {
}
