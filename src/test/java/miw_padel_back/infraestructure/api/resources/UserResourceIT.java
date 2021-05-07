package miw_padel_back.infraestructure.api.resources;

import miw_padel_back.RestTestConfig;
import miw_padel_back.RoleBuilder;
import miw_padel_back.configuration.security.JWTUtil;
import miw_padel_back.domain.models.Gender;
import miw_padel_back.domain.models.Role;
import miw_padel_back.infraestructure.api.dtos.UserLoginDto;
import miw_padel_back.infraestructure.api.dtos.TokenDto;
import miw_padel_back.infraestructure.api.dtos.UserRegisterDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static miw_padel_back.infraestructure.api.resources.UserResource.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RestTestConfig
 class UserResourceIT {

    private static final String FIRST_NAME = "testNameIT";
    private static final String FAMILY_NAME = "testFamilyNameIT";
    private static final String EMAIL = "testIT@test.test";
    private static final String PASSWORD = "testPasswordIT";

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private JWTUtil jwtUtil;


    @Test
    void testGivenEmailAndPasswordWhenLoginThenReturnCorrectJWT(){
        this.webTestClient
                .post()
                .uri(USER+AUTH)
                .body(Mono.just(new UserLoginDto("lusky1996@gmail.com","11111")), UserLoginDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TokenDto.class)
                .value(tokenDto -> {
                    assertEquals("ADMIN",jwtUtil.getAllClaimsFromToken(tokenDto.getToken()));

                });
    }

    @Test
    void testGivenUserWhenRegisterThenReturnUser(){
        LocalDateTime localDateTime = LocalDateTime.of(1996,11,20,0,0,0);
        UserRegisterDto user = UserRegisterDto.builder()
                .firstName(FIRST_NAME)
                .familyName(FAMILY_NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .gender(Gender.MALE)
                .roles(new RoleBuilder().addAdminRole().addPlayerRole().build())
                .birthDate(localDateTime).build();

        this.webTestClient
                .post()
                .uri(USER+REGISTER)
                .body(Mono.just(user), UserRegisterDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserRegisterDto.class)
                .value(saveUser -> {
                    assertEquals(FIRST_NAME,saveUser.getFirstName());
                    assertEquals(FAMILY_NAME,saveUser.getFamilyName());
                    assertEquals(EMAIL,saveUser.getEmail());
                    assertEquals("",saveUser.getPassword());
                    assertEquals(Gender.MALE,saveUser.getGender());
                    assertTrue(saveUser.getRoles().contains(Role.ROLE_ADMIN));
                    assertTrue(saveUser.getRoles().contains(Role.ROLE_PLAYER));
                });
    }

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
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);
    }




}
