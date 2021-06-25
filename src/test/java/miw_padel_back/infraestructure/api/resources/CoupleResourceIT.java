package miw_padel_back.infraestructure.api.resources;

import miw_padel_back.domain.models.Role;
import miw_padel_back.infraestructure.api.RestClientTestService;
import miw_padel_back.infraestructure.api.dtos.CoupleDto;
import miw_padel_back.infraestructure.api.dtos.EmailDto;
import miw_padel_back.infraestructure.api.dtos.IdDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

import static miw_padel_back.infraestructure.api.resources.CoupleResource.ACCEPTANCE;
import static miw_padel_back.infraestructure.api.resources.CoupleResource.COUPLE;

@RestTestConfig
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CoupleResourceIT {

    private static final String ID_PARAM = "?id={id}";


    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private RestClientTestService restClientTestService;

    private static AtomicReference<String> atomicReference;

    @BeforeAll
    static void setUp() {
        atomicReference = new AtomicReference<>("NULL");
    }

    @Test
    @Order(1)
    void testGivenEmailWhenCreateThenReturnCouple() {
        this.restClientTestService.login("captain@player.com", webTestClient, Role.ROLE_PLAYER)
                .post()
                .uri(COUPLE)
                .body(Mono.just(EmailDto.builder().email("player@player.com").build()), EmailDto.class)
                .exchange()
                .expectBody(CoupleDto.class)
                .value(coupleDto -> atomicReference.set(coupleDto.getId()));
    }

    @Test
    @Order(2)
    void testGivenIdWhenDeleteThenReturnConflict() {
        this.restClientTestService.login("player@player.com", webTestClient, Role.ROLE_PLAYER)
                .delete()
                .uri(COUPLE + ID_PARAM, atomicReference.get())
                .exchange()
                .expectStatus()
                .isOk();
        this.testGivenEmailWhenCreateThenReturnCouple();
    }

    @Test
    @Order(3)
    void testGivenEmailWhenAcceptPetitionThenReturnOK() {
        this.restClientTestService.login("player@player.com", webTestClient, Role.ROLE_PLAYER)
                .put()
                .uri(COUPLE)
                .body(Mono.just(IdDto.builder().id(atomicReference.get()).build()), IdDto.class)
                .exchange()
                .expectStatus()
                .isOk();
    }


    @Test
    @Order(4)
    void testWhenGetCouplesThenReturnOK() {
        this.restClientTestService.login("captain@captail.com", webTestClient, Role.ROLE_PLAYER)
                .get()
                .uri(COUPLE)
                .exchange()
                .expectBodyList(CoupleDto.class);
    }

    @Test
    @Order(5)
    void testWhenDeleteWithNotOwnerPlayerThenReturn() {
        this.restClientTestService.login("notOwner@player.com", webTestClient, Role.ROLE_PLAYER)
                .delete()
                .uri(COUPLE + ID_PARAM, atomicReference.get())
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.CONFLICT);
    }
}
