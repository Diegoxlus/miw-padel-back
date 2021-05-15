package miw_padel_back.infraestructure.api.resources;

import miw_padel_back.domain.models.PaddleCourt;
import miw_padel_back.domain.services.PaddleCourtService;
import miw_padel_back.infraestructure.api.dtos.PaddleCourtAvailabilityDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping(PaddleCourtResource.PADDLE_COURT)
public class PaddleCourtResource {
    public static final String PADDLE_COURT = "paddle-court";
    public static final String NAME_REF = "/{nameRef}";
    public static final String DATE_REF = "/{dateRef}";



    private final PaddleCourtService paddleCourtService;

    @Autowired
    public PaddleCourtResource(PaddleCourtService paddleCourtService) {
        this.paddleCourtService = paddleCourtService;
    }

    @GetMapping()
    public Flux<PaddleCourt> readAll(){
       return this.paddleCourtService.readAll();
    }

    @GetMapping(PADDLE_COURT+NAME_REF+DATE_REF)
    public Mono<PaddleCourtAvailabilityDto> readAvailabilityByNameAndDate(@RequestParam() String nameReference, @RequestParam String dateReference) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat();
        var date = dateFormat.parse(dateReference);
        return this.paddleCourtService.readAvailabilityByNameAndDate(nameReference,date);
    }
}
