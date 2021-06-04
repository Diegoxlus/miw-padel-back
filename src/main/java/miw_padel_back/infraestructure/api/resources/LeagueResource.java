package miw_padel_back.infraestructure.api.resources;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import miw_padel_back.domain.models.League;
import miw_padel_back.domain.services.CoupleService;
import miw_padel_back.domain.services.LeagueService;
import miw_padel_back.infraestructure.api.dtos.LeagueDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping(LeagueResource.LEAGUE)
public class LeagueResource {
    public static final String LEAGUE = "/league";

    @Autowired
    private final LeagueService leagueService;

    public LeagueResource(LeagueService leagueService) {
        this.leagueService = leagueService;
    }

    @GetMapping
    @PreAuthorize("hasRole('PLAYER') or hasRole('ADMIN')")
    public Flux<LeagueDto> readAll(){
        return this.leagueService.readAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<LeagueDto> create(@RequestBody League league) {
        return this.leagueService.create(league);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Void> create(@RequestParam String id) {
        return this.leagueService.delete(id);
    }


}
