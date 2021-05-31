package miw_padel_back.infraestructure.api.resources;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import miw_padel_back.domain.models.Couple;
import miw_padel_back.domain.services.CoupleService;
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

    private final CoupleService coupleService;

    @Autowired
    public CoupleResource(CoupleService coupleService) {
        this.coupleService = coupleService;
    }

    @GetMapping()
    public Flux<Couple> readPlayerCouples(){
        return  ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication)
                .flatMapMany(authentication -> this.coupleService.readPlayerCouples(authentication.getPrincipal().toString()));
    }

    @PostMapping
    public Mono<Couple> createCouplePetition(@RequestBody EmailDto emailDto){
        return  ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication)
                .flatMap(authentication -> this.coupleService.createCouplePetition(authentication.getPrincipal().toString(),emailDto));
    }

    @PostMapping(value = "/acceptance")
    public Mono<Couple> acceptCouplePetition(@RequestBody IdDto idDto){
        return  ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication)
                .flatMap(authentication -> this.coupleService.acceptCouplePetition(authentication.getPrincipal().toString(),idDto));
    }
}
