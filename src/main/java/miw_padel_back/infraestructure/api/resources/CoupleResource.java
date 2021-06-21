package miw_padel_back.infraestructure.api.resources;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import miw_padel_back.domain.services.CoupleService;
import miw_padel_back.infraestructure.api.dtos.CoupleDto;
import miw_padel_back.infraestructure.api.dtos.EmailDto;
import miw_padel_back.infraestructure.api.dtos.IdDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('PLAYER') or hasRole('ADMIN')")
@RestController
@RequestMapping(CoupleResource.COUPLE)
public class CoupleResource {
    public static final String COUPLE = "/couple";
    public static final String ACCEPTANCE = "/acceptance";

    private final CoupleService coupleService;

    @Autowired
    public CoupleResource(CoupleService coupleService) {
        this.coupleService = coupleService;
    }

    @GetMapping()
    public Flux<CoupleDto> readPlayerCouples() {
        return ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication)
                .flatMapMany(authentication -> this.coupleService.readPlayerCouples(authentication.getPrincipal().toString()));
    }

    @PostMapping
    public Mono<CoupleDto> createCouplePetition(@RequestBody EmailDto emailDto) {
        return ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication)
                .flatMap(authentication -> this.coupleService.createCouplePetition(authentication.getPrincipal().toString(), emailDto));
    }

    @PostMapping(value = ACCEPTANCE)
    public Mono<CoupleDto> acceptCouplePetition(@RequestBody IdDto idDto) {
        return ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication)
                .flatMap(authentication -> this.coupleService.acceptCouplePetition(authentication.getPrincipal().toString(), idDto));
    }

    @DeleteMapping()
    public Mono<Void> deleteCouplePetition(@RequestParam String id) {
        return ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication)
                .flatMap(authentication -> this.coupleService.deleteCouplePetition(authentication.getPrincipal().toString(), id));
    }
}
