package miw_padel_back.infraestructure.api.resources;

import miw_padel_back.domain.models.PaddleCourt;
import miw_padel_back.domain.models.PaddleCourtType;
import miw_padel_back.infraestructure.api.RestClientTestService;
import miw_padel_back.infraestructure.api.dtos.PaddleCourtAvailabilityDto;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicReference;

import static miw_padel_back.infraestructure.api.resources.PaddleCourtResource.AVAILABLE;
import static miw_padel_back.infraestructure.api.resources.PaddleCourtResource.PADDLE_COURT;

@RestTestConfig
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PaddleCourtResourceIT {
    private static final String NAME = "NEW COURT";
    public static final String NAME_PARAM = "?name={name}";
    public static final String DATE_PARAM = "?date={date}";

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private RestClientTestService restClientTestService;


    PaddleCourt paddleCourt = PaddleCourt.builder()
            .name(NAME)
            .paddleCourtType(PaddleCourtType.INDOOR)
            .disabled(false)
            .startTime("09:00").startTime("10:30").startTime("12:00").startTime("13:30").startTime("15:00").startTime("16:30")
            .endTime("10:30").endTime("12:00").endTime("13:30").endTime("15:00").endTime("16:30").endTime("18:00")
            .build();

    private static final AtomicReference<String> id = new AtomicReference<>("");

    @Test
    @Order(1)
    void testGivenPaddleCourtWhenPostThenReturnOk() {
        this.restClientTestService.loginAdmin(webTestClient)
                .post()
                .uri(PADDLE_COURT)
                .body(Mono.just(paddleCourt), PaddleCourt.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(PaddleCourt.class)
                .value(paddleCourtBody -> id.set(paddleCourtBody.getId()));
    }

    @Test
    @Order(2)
    void testGivenPaddleCourtWhenPutThenReturnOk() {
        this.paddleCourt.setId(id.get());
        this.restClientTestService.loginAdmin(webTestClient)
                .put()
                .uri(PADDLE_COURT)
                .body(Mono.just(paddleCourt), PaddleCourt.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    @Order(3)
    void testGivenPaddleCourtNameWhenDeleteThenReturnMonoVoid() {
        this.restClientTestService.loginAdmin(webTestClient)
                .delete()
                .uri(PADDLE_COURT + NAME_PARAM, paddleCourt.getName())
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void testWhenGetThenReturnFluxOfPaddleCourt() {
        this.restClientTestService.loginAdmin(webTestClient)
                .get()
                .uri(PADDLE_COURT)
                .exchange()
                .expectBodyList(PaddleCourt.class);
    }

    @Test
    void testWhenGetAvailableThenReturnFluxOfPaddleCourtAvailabilityDto() {
        this.restClientTestService.loginAdmin(webTestClient)
                .get()
                .uri(PADDLE_COURT + AVAILABLE + DATE_PARAM, LocalDate.EPOCH)
                .exchange()
                .expectBodyList(PaddleCourtAvailabilityDto.class);
    }
}
