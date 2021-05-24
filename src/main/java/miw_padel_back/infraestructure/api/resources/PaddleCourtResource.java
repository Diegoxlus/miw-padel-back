package miw_padel_back.infraestructure.api.resources;

import miw_padel_back.domain.models.PaddleCourt;
import miw_padel_back.domain.services.PaddleCourtService;
import miw_padel_back.infraestructure.api.dtos.PaddleCourtAvailabilityDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import java.time.LocalDate;

@RestController
@RequestMapping(PaddleCourtResource.PADDLE_COURT)
public class PaddleCourtResource {
    public static final String PADDLE_COURT = "/paddle-court";
    public static final String NAME_REF = "/{nameRef}";
    public static final String AVAILABLE = "/available";


    private final PaddleCourtService paddleCourtService;

    @Autowired
    public PaddleCourtResource(PaddleCourtService paddleCourtService) {
        this.paddleCourtService = paddleCourtService;
    }

    @GetMapping()
    public Flux<PaddleCourt> readAll() {
        return this.paddleCourtService.readAll();
    }

    @GetMapping(AVAILABLE)
    public Flux<PaddleCourtAvailabilityDto> readAvailabilityByDate(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return this.paddleCourtService.readAvailabilityByDate(date);
    }
}
