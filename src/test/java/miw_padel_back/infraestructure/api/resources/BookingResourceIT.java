package miw_padel_back.infraestructure.api.resources;

import miw_padel_back.domain.models.Role;
import miw_padel_back.infraestructure.api.RestClientTestService;
import miw_padel_back.infraestructure.api.dtos.BookingDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static miw_padel_back.infraestructure.api.resources.BookingResource.BOOKING;


@RestTestConfig
class BookingResourceIT {

    private static final String DATE_PARAM = "?date={date}";

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private RestClientTestService restClientTestService;


    @Test
    void testGivenPlayerWhenGetBookingsThenReturnOK() {

        this.restClientTestService.login("player@player.com", webTestClient, Role.ROLE_PLAYER)
                .get()
                .uri(BOOKING)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(BookingDto.class);
    }

    @Test
    void testGivenPlayerWhenGetBookingsByDateThenReturnOK() {

        this.restClientTestService.login("player@player.com", webTestClient, Role.ROLE_PLAYER)
                .get()
                .uri(BOOKING + DATE_PARAM, LocalDate.EPOCH)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(BookingDto.class);
    }

    @Test
    void testGivenAdminWhenGetBookingsThenReturnOK() {

        this.restClientTestService.loginAdmin(webTestClient)
                .get()
                .uri(BOOKING)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(BookingDto.class);
    }

    @Test
    void testGivenAdminWhenGetBookingsByDateThenReturnOK() {

        this.restClientTestService.login("admin@admin.com", webTestClient, Role.ROLE_ADMIN)
                .get()
                .uri(BOOKING + DATE_PARAM, LocalDate.EPOCH)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(BookingDto.class);
    }

    @Test
    void testGivenUnauthorizedWhenGetBookingsThenReturn401() {

        this.restClientTestService.login("unauthorized@unauthorized.com", webTestClient, Role.ROLE_NULL)
                .get()
                .uri(BOOKING)
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }

    @Test
    void testGivenBookingDtoWhenPostThenReturnCreated() {
        BookingDto bookingDto = BookingDto.builder()
                .date(LocalDate.EPOCH)
                .email("player@player.com")
                .paddleCourtName("PC 2")
                .timeRange("10:00 - 12:00")
                .build();

        this.restClientTestService.login("player@player.com", webTestClient, Role.ROLE_PLAYER)
                .post()
                .uri(BOOKING)
                .body(Mono.just(bookingDto), BookingDto.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void testGivenExistentBookingDtoWhenPostThenReturnConflict() {
        BookingDto bookingDto = BookingDto.builder()
                .date(LocalDate.EPOCH)
                .email("player@player.com")
                .paddleCourtName("PC 1")
                .timeRange("10:00 - 12:00")
                .build();

        this.restClientTestService.login("player@player.com", webTestClient, Role.ROLE_PLAYER)
                .post()
                .uri(BOOKING)
                .body(Mono.just(bookingDto), BookingDto.class)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.CONFLICT);
    }
}
