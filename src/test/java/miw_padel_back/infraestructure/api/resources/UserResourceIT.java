package miw_padel_back.infraestructure.api.resources;

import miw_padel_back.RoleBuilder;
import miw_padel_back.configuration.security.JWTUtil;
import miw_padel_back.domain.models.Gender;
import miw_padel_back.domain.models.Role;
import miw_padel_back.infraestructure.api.RestClientTestService;
import miw_padel_back.infraestructure.api.dtos.TokenDto;
import miw_padel_back.infraestructure.api.dtos.UserLoginDto;
import miw_padel_back.infraestructure.api.dtos.UserRegisterDto;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

import static miw_padel_back.infraestructure.api.resources.UserResource.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@RestTestConfig
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserResourceIT {

    private static final String FIRST_NAME = "testNameIT";
    private static final String FAMILY_NAME = "testFamilyNameIT";
    private static final String EMAIL = "testIT@test.test";
    private static final String PASSWORD = "testPasswordIT";

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private RestClientTestService restClientTestService;

    @Autowired
    private JWTUtil jwtUtil;


    @Test
    void testGivenEmailAndPasswordWhenLoginThenReturnCorrectJWT() {
        this.webTestClient
                .post()
                .uri(USER + AUTH)
                .body(Mono.just(new UserLoginDto("admin@admin.com", "11111")), UserLoginDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TokenDto.class)
                .value(tokenDto -> {
                    var roleStringList = this.jwtUtil.getAllClaimsFromToken(tokenDto.getToken()).get("role", List.class);
                    String jwtToken = tokenDto.getToken();

                    assertEquals("admin@admin.com", this.jwtUtil.getUsernameFromToken(jwtToken));
                    assertTrue(roleStringList.contains(Role.ROLE_ADMIN.name()));
                    assertFalse(jwtUtil.isTokenExpired(jwtToken));
                });
    }

    @Test
    @Order(1)
    void testGivenUserWhenRegisterThenReturnUser() {
        LocalDate date = LocalDate.of(1996, Calendar.NOVEMBER, 11);
        UserRegisterDto user = UserRegisterDto.builder()
                .firstName(FIRST_NAME)
                .familyName(FAMILY_NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .gender(Gender.MALE)
                .roles(new RoleBuilder().addAdminRole().addPlayerRole().build())
                .birthDate(date)
                .build();

        this.webTestClient
                .post()
                .uri(USER + REGISTER)
                .body(Mono.just(user), UserRegisterDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserRegisterDto.class)
                .value(saveUser -> {
                    assertEquals(FIRST_NAME, saveUser.getFirstName());
                    assertEquals(FAMILY_NAME, saveUser.getFamilyName());
                    assertEquals(EMAIL, saveUser.getEmail());
                    assertEquals("", saveUser.getPassword());
                    assertEquals(Gender.MALE, saveUser.getGender());
                    assertEquals(date, saveUser.getBirthDate());
                    assertTrue(saveUser.getRoles().contains(Role.ROLE_ADMIN));
                    assertTrue(saveUser.getRoles().contains(Role.ROLE_PLAYER));
                });
    }


    @Test
    @Order(2)
    void testGivenPartFileWhenSaveImageThenReturn200OK() {
        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
        multipartBodyBuilder.part("file", new ClassPathResource("/img/test.png"));
        this.restClientTestService.loginAdmin(webTestClient)
                .post()
                .uri(USER + PHOTO)
                .body(BodyInserters.fromMultipartData(multipartBodyBuilder.build()))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @Order(3)
    void testEmailWhenLoadImageThenReturnByteArray() {
        var emailParam = "?email={email}";
        this.restClientTestService.loginAdmin(webTestClient)
                .get()
                .uri(USER + PHOTO + emailParam  ,"admin@admin.com")
                .exchange()
                .expectStatus().isOk();
    }

    /*

    @Test
    void testGivenUserWithoutEmailWhenRegisterThenReturnError(){
        LocalDateTime localDateTime = LocalDateTime.of(1996,11,20,0,0,0);
        UserRegisterDto user = UserRegisterDto.builder()
                .firstName(FIRST_NAME)
                .familyName(FAMILY_NAME)
                .password(PASSWORD)
                .gender(Gender.MALE)
                .roles(new RoleBuilder().addAdminRole().addPlayerRole().build())
                .birthDate(localDateTime).build();

        this.webTestClient
                .post()
                .uri(USER+REGISTER)
                .body(Mono.just(user), UserRegisterDto.class)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST);
    }
    */

}
