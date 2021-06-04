package miw_padel_back.infraestructure.mongodb.persistence;

import miw_padel_back.domain.exceptions.NotFoundException;
import miw_padel_back.domain.models.League;
import miw_padel_back.domain.persistence.LeaguePersistence;
import miw_padel_back.infraestructure.api.dtos.LeagueDto;
import miw_padel_back.infraestructure.mongodb.daos.reactive.LeagueReactive;
import miw_padel_back.infraestructure.mongodb.entities.LeagueEntity;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class LeaguePersistenceMDB implements LeaguePersistence {

    private final LeagueReactive leagueReactive;

    public LeaguePersistenceMDB(LeagueReactive leagueReactive) {
        this.leagueReactive = leagueReactive;
    }

    @Override
    public Flux<LeagueDto> readAll() {
        return this.leagueReactive.findAll()
                .map(LeagueEntity::toLeagueDto);
    }

    @Override
    public Mono<LeagueDto> create(League league) {
        return this.leagueReactive.save(new LeagueEntity(league))
                .map(LeagueEntity::toLeagueDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        return this.leagueReactive.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("League with this id not exists")))
                .flatMap(this.leagueReactive::delete);
    }
}
