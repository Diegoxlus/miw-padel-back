package miw_padel_back.infraestructure.mongodb.persistence;

import miw_padel_back.RoleBuilder;
import miw_padel_back.TestConfig;
import miw_padel_back.domain.exceptions.ConflictException;
import miw_padel_back.domain.exceptions.NotFoundException;
import miw_padel_back.domain.models.Gender;
import miw_padel_back.domain.models.Role;
import miw_padel_back.infraestructure.api.dtos.UserLoginDto;
import miw_padel_back.infraestructure.api.dtos.UserRegisterDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestConfig
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserPersistenceMDBTest {
    private static final String FIRST_NAME = "testName";
    private static final String FAMILY_NAME = "testFamilyName";
    private static final String EMAIL = "test@test.test";
    private static final String PASSWORD = "testPassword";
    private static final byte[] BYTES = new byte[]{1, 0, 1, 0};

    private static UserRegisterDto user;


    @Autowired
    private UserPersistenceMDB userPersistenceMDB;

    @BeforeAll
    public static void setUp() {
        LocalDate date = LocalDate.of(1996, Calendar.NOVEMBER, 11);
        user = UserRegisterDto.builder()
                .firstName(FIRST_NAME)
                .familyName(FAMILY_NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .gender(Gender.MALE)
                .roles(new RoleBuilder().addAdminRole().addPlayerRole().build())
                .birthDate(date).build();
    }

    @Test
    @Order(1)
    void testGivenUserWhenCreateThenReturnUser() {
        StepVerifier
                .create(this.userPersistenceMDB.create(user))
                .expectNextMatches(saveUser -> {
                    assertEquals(FIRST_NAME, saveUser.getFirstName());
                    assertEquals(FAMILY_NAME, saveUser.getFamilyName());
                    assertEquals(EMAIL, saveUser.getEmail());
                    assertEquals("", saveUser.getPassword());
                    assertEquals(Gender.MALE, saveUser.getGender());
                    assertTrue(saveUser.getRoles().contains(Role.ROLE_ADMIN));
                    assertTrue(saveUser.getRoles().contains(Role.ROLE_PLAYER));
                    return true;
                })
                .expectComplete()
                .verify();
    }


    @Test
    @Order(2)
    void testGivenEmailAndPasswordWhenLoginThenReturnUser() {
        StepVerifier
                .create(this.userPersistenceMDB.login(new UserLoginDto(EMAIL, PASSWORD)))
                .expectNextMatches(user -> {
                    assertEquals(FIRST_NAME, user.getFirstName());
                    assertEquals(FAMILY_NAME, user.getFamilyName());
                    assertEquals(EMAIL, user.getEmail());
                    assertEquals(Gender.MALE, user.getGender());
                    assertTrue(user.getRoles().contains(Role.ROLE_ADMIN));
                    return true;
                })
                .expectComplete()
                .verify();
    }

    @Test
    void testGivenInvalidEmailAndValidPasswordWhenLoginThenReturnNotFoundException() {
        String invalidEmail = "invalid@invalid.invalid";
        StepVerifier
                .create(this.userPersistenceMDB.login(new UserLoginDto(invalidEmail, PASSWORD)))
                .expectErrorMatches(throwable -> throwable instanceof NotFoundException &&
                        throwable.getMessage().equals("Not Found Exception: Not existent email " + invalidEmail))
                .verify();
    }

    @Test
    @Order(3)
    void testGivenValidEmailAndInvalidPasswordWhenLoginThenReturnConflictException() {
        StepVerifier
                .create(this.userPersistenceMDB.login(new UserLoginDto(EMAIL, "invalidPassword")))
                .expectErrorMatches(throwable -> throwable instanceof ConflictException &&
                        throwable.getMessage().equals("Conflict Exception: Incorrect password"))
                .verify();
    }

    @Test
    @Order(4)
    void testGivenValidEmailAndByteArrayWhenSaveImageThenReturn() {
        StepVerifier
                .create(this.userPersistenceMDB.saveImage(EMAIL, BYTES))
                .expectComplete()
                .verify();
    }

    @Test
    @Order(5)
    void testGivenValidEmailWhenLoadImageThenReturnByteArray() {
        StepVerifier
                .create(this.userPersistenceMDB.loadImage(EMAIL))
                .expectNextMatches(bytes -> {
                    assertTrue(Arrays.equals(bytes, BYTES));
                    return true;
                })
                .expectComplete()
                .verify();
    }
}
