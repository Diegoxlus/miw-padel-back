package miw_padel_back.infraestructure.api.resources;

import miw_padel_back.domain.models.PaddleCourt;
import miw_padel_back.domain.services.PaddleCourtService;
import miw_padel_back.infraestructure.api.dtos.PaddleCourtAvailabilityDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RestController
@RequestMapping(PaddleCourtResource.PADDLE_COURT)
public class PaddleCourtResource {
    public static final String PADDLE_COURT = "/paddle-court";
    public static final String AVAILABLE = "/available";


    private final PaddleCourtService paddleCourtService;

    @Autowired
    public PaddleCourtResource(PaddleCourtService paddleCourtService) {
        this.paddleCourtService = paddleCourtService;
    }

    @PostMapping()
    public Mono<PaddleCourt> create(@RequestBody PaddleCourt paddleCourt){
        return this.paddleCourtService.create(paddleCourt);
    }

    @GetMapping()
    public Flux<PaddleCourt> readAll() {
        return this.paddleCourtService.readAllOrderByName();
    }

    @GetMapping(AVAILABLE)
    public Flux<PaddleCourtAvailabilityDto> readAvailabilityByDate(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return this.paddleCourtService.readAvailabilityByDate(date);
    }

    @PutMapping()
    public Mono<PaddleCourt> edit(@RequestBody PaddleCourt paddleCourt){
        return this.paddleCourtService.edit(paddleCourt);
    }

    @DeleteMapping()
    public Mono<Void> delete(@RequestParam String name){
        return this.paddleCourtService.delete(name);
    }
}
