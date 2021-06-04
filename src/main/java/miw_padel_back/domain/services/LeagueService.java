package miw_padel_back.domain.services;

import miw_padel_back.domain.models.League;
import miw_padel_back.domain.persistence.LeaguePersistence;
import miw_padel_back.infraestructure.api.dtos.LeagueDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class LeagueService {
    @Autowired
    private final LeaguePersistence leaguePersistence;

    public LeagueService(LeaguePersistence leaguePersistence) {
        this.leaguePersistence = leaguePersistence;
    }

    public Flux<LeagueDto> readAll() {
        return this.leaguePersistence.readAll();
    }

    public Mono<LeagueDto> create(League league) {
    return this.leaguePersistence.create(league);
    }

    public Mono<Void> delete(String id) {
    return this.leaguePersistence.delete(id);
    }
}
