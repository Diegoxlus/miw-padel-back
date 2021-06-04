package miw_padel_back.domain.services;

import miw_padel_back.domain.persistence.LeaguePersistence;
import org.springframework.beans.factory.annotation.Autowired;

public class LeagueService {
    @Autowired
    private final LeaguePersistence leaguePersistence;

    public LeagueService(LeaguePersistence leaguePersistence) {
        this.leaguePersistence = leaguePersistence;
    }
}
