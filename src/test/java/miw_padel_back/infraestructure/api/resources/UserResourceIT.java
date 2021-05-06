package miw_padel_back.infraestructure.api.resources;

import miw_padel_back.RestTestConfig;
import miw_padel_back.infraestructure.api.dtos.UserLoginDto;
import miw_padel_back.infraestructure.api.dtos.TokenDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static miw_padel_back.infraestructure.api.resources.UserResource.AUTH;
import static miw_padel_back.infraestructure.api.resources.UserResource.USER;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RestTestConfig
 class UserResourceIT {

    @Autowired
    private WebTestClient webTestClient;


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
                    assertNotNull(tokenDto.getToken());
                });
    }




}
