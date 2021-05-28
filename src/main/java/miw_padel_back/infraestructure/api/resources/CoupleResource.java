package miw_padel_back.infraestructure.api.resources;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import miw_padel_back.domain.models.Couple;
import miw_padel_back.domain.services.CoupleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('PLAYER')")
@RestController
public class CoupleResource {
    public static String COUPLE = "/couple";

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
}
