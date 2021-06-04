package miw_padel_back.domain.persistence;

import miw_padel_back.infraestructure.api.dtos.CoupleDto;
import miw_padel_back.infraestructure.api.dtos.EmailDto;
import miw_padel_back.infraestructure.api.dtos.IdDto;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CouplePersistence {
    Flux<CoupleDto> readPlayerCouples(String email);

    Mono<CoupleDto> createCouplePetition(String emailCaptain, EmailDto emailDto);

    Mono<CoupleDto> acceptCouplePetition(String playerEmail, IdDto idDto);

    Mono<Void> deleteCouplePetition(String playerEmail, String id);
}
