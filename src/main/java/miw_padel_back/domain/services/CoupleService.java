package miw_padel_back.domain.services;

import miw_padel_back.domain.models.Couple;
import miw_padel_back.domain.persistence.CouplePersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class CoupleService {
    @Autowired
    private final CouplePersistence couplePersistence;

    public CoupleService(CouplePersistence couplePersistence) {
        this.couplePersistence = couplePersistence;
    }

    public Flux<Couple> readPlayerCouples(String email){
        return this.couplePersistence.readPlayerCouples(email);
    }
}
