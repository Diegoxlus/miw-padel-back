package miw_padel_back.domain.persistence;

import miw_padel_back.domain.models.Couple;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CouplePersistence {
    Flux<Couple> readPlayerCouples(String email);
}
