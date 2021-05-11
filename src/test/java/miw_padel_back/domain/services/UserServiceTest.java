package miw_padel_back.domain.services;

import miw_padel_back.TestConfig;
import miw_padel_back.configuration.security.JWTUtil;
import miw_padel_back.domain.models.Role;
import miw_padel_back.infraestructure.api.dtos.UserLoginDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
public class UserServiceTest {
    private static final String EMAIL = "lusky1996@gmail.com";

    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtil jwtUtil;

    @Test
    void testGivenEmailAndPasswordWhenLoginThenReturnCorrectJWT() {
        StepVerifier
                .create(this.userService.login(new UserLoginDto(EMAIL,"11111")))
                .expectNextMatches(tokenDto -> {
                    var roleStringList = this.jwtUtil.getAllClaimsFromToken(tokenDto.getToken()).get("role", List.class);
                    String jwtToken = tokenDto.getToken();

                    assertEquals(EMAIL, this.jwtUtil.getUsernameFromToken(jwtToken));
                    assertTrue(roleStringList.contains(Role.ROLE_ADMIN.name()));
                    assertFalse(jwtUtil.isTokenExpired(jwtToken));
                    return true;
                })
                .expectComplete()
                .verify();
    }
}
