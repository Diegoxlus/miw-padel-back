package miw_padel_back.domain.persistence;

import miw_padel_back.domain.models.League;
import miw_padel_back.infraestructure.api.dtos.LeagueDto;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface LeaguePersistence {
    Flux<LeagueDto> readAll();

    Mono<LeagueDto> create(League league);

    Mono<Void> delete(String id);
}
