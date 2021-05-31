package miw_padel_back.domain.persistence;

import miw_padel_back.domain.models.Couple;
import miw_padel_back.infraestructure.api.dtos.EmailDto;
import miw_padel_back.infraestructure.api.dtos.IdDto;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CouplePersistence {
    Flux<Couple> readPlayerCouples(String email);
    Mono<Couple> createCouplePetition(String emailCaptain, EmailDto emailDto);
    Mono<Couple> acceptCouplePetition(String playerEmail, IdDto idDto);
}
