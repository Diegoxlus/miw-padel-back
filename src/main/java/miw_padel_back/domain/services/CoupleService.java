package miw_padel_back.domain.services;

import miw_padel_back.domain.models.Couple;
import miw_padel_back.domain.persistence.CouplePersistence;
import miw_padel_back.infraestructure.api.dtos.CoupleDto;
import miw_padel_back.infraestructure.api.dtos.EmailDto;
import miw_padel_back.infraestructure.api.dtos.IdDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CoupleService {
    @Autowired
    private final CouplePersistence couplePersistence;

    public CoupleService(CouplePersistence couplePersistence) {
        this.couplePersistence = couplePersistence;
    }

    public Flux<CoupleDto> readPlayerCouples(String email){
        return this.couplePersistence.readPlayerCouples(email);
    }

    public Mono<CoupleDto> createCouplePetition(String emailCaptain, EmailDto emailDto) {
        return this.couplePersistence.createCouplePetition(emailCaptain,emailDto);
    }

    public Mono<CoupleDto> acceptCouplePetition(String playerEmail, IdDto idDto) {
        return this.couplePersistence.acceptCouplePetition(playerEmail,idDto);
    }
}
