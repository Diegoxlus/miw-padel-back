package miw_padel_back.infraestructure.mongodb.daos.reactive;

import miw_padel_back.infraestructure.mongodb.entities.LeagueEntity;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;

public interface LeagueReactive extends ReactiveSortingRepository<LeagueEntity, String> {
}
