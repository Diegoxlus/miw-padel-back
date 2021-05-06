package miw_padel_back.infraestructure.mongodb.persistence;

import miw_padel_back.RoleBuilder;
import miw_padel_back.TestConfig;
import miw_padel_back.configuration.security.JWTUtil;
import miw_padel_back.domain.exceptions.ConflictException;
import miw_padel_back.domain.exceptions.NotFoundException;
import miw_padel_back.infraestructure.api.dtos.UserLoginDto;
import miw_padel_back.domain.models.Gender;
import miw_padel_back.domain.models.Role;
import miw_padel_back.domain.models.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserPersistenceMDBTest {
    private static final String FIRST_NAME = "testName";
    private static final String FAMILY_NAME = "testFamilyName";
    private static final String EMAIL = "test@test.test";
    private static final String PASSWORD = "testPassword";

    private static User user;

    @Autowired
    private UserPersistenceMDB userPersistenceMDB;

    @Autowired
    private JWTUtil jwtUtil;

    @BeforeAll
    public static void setUp(){
        LocalDateTime localDateTime = LocalDateTime.of(1996,11,11,0,0,0);
         user = User.builder()
                 .firstName(FIRST_NAME)
                 .familyName(FAMILY_NAME)
                 .email(EMAIL)
                 .password(PASSWORD)
                 .gender(Gender.MALE)
                 .roles(new RoleBuilder().addAdminRole().addPlayerRole().build())
                 .birthDate(localDateTime).build();
    }

    @Test
    @Order(1)
    void testGivenUserWhenCreateThenReturnUser() {
        StepVerifier
                .create(this.userPersistenceMDB.create(user))
                .expectNextMatches(saveUser -> {
                    assertEquals(FIRST_NAME,saveUser.getFirstName());
                    assertEquals(FAMILY_NAME,saveUser.getFamilyName());
                    assertEquals(EMAIL,saveUser.getEmail());
                    assertEquals("",saveUser.getPassword());
                    assertEquals(Gender.MALE,saveUser.getGender());
                    assertTrue(saveUser.getRoles().contains(Role.ADMIN));
                    assertTrue(saveUser.getRoles().contains(Role.PLAYER));
                    return true;
                })
                .expectComplete()
                .verify();
    }

    @Test
    void testGivenInvalidUserWhenCreateThenReturnError() {
        StepVerifier
                .create(this.userPersistenceMDB.create(User.builder().build()))
                .expectErrorMatches(throwable -> throwable instanceof ConflictException &&
                throwable.getMessage().equals("Conflict Exception: Empty fields"))
                .verify();
    }

    @Test
    @Order(2)
    void testGivenEmailAndPasswordWhenLoginThenReturnCorrectJWT() {
        StepVerifier
                .create(this.userPersistenceMDB.login(new UserLoginDto(EMAIL,PASSWORD)))
                .expectNextMatches(tokenDto -> {
                    var roleStringList = this.jwtUtil.getAllClaimsFromToken(tokenDto.getToken()).get("role", List.class);
                    String jwtToken = tokenDto.getToken();

                    assertEquals(EMAIL, this.jwtUtil.getUsernameFromToken(jwtToken));
                    assertTrue(roleStringList.contains(Role.ADMIN.name()));
                    assertTrue(roleStringList.contains(Role.PLAYER.name()));
                    assertFalse(jwtUtil.isTokenExpired(jwtToken));
                    return true;
                })
                .expectComplete()
                .verify();
    }

    @Test
    void testGivenInvalidEmailAndValidPasswordWhenLoginThenReturnNotFoundException() {
        String invalidEmail = "invalid@invalid.invalid";
        StepVerifier
                .create(this.userPersistenceMDB.login(new UserLoginDto(invalidEmail,PASSWORD)))
                .expectErrorMatches(throwable -> throwable instanceof NotFoundException &&
                        throwable.getMessage().equals("Not Found Exception: Not existent email "+ invalidEmail ))
                .verify();
    }

    @Test
    @Order(3)
    void testGivenValidEmailAndInvalidPasswordWhenLoginThenReturnConflictException() {
        StepVerifier
                .create(this.userPersistenceMDB.login(new UserLoginDto(EMAIL,"invalidPassword")))
                .expectErrorMatches(throwable -> throwable instanceof ConflictException &&
                        throwable.getMessage().equals("Conflict Exception: Incorrect password"))
                .verify();
    }
}
