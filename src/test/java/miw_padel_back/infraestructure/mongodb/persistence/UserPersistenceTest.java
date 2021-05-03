package miw_padel_back.infraestructure.mongodb.persistence;

import miw_padel_back.TestConfig;
import miw_padel_back.domain.model.Gender;
import miw_padel_back.domain.model.User;
import miw_padel_back.infraestructure.mongodb.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestConfig
public class UserPersistenceTest {
    @Autowired
    private UserPersistenceMDB userPersistenceMDB;

    @Test
    void testGivenUserWhenCreateThenReturnUser() {
        User user = User.builder().firstName("Diego22").familyName("Lusqui22").email("lusky191296@gmail.com")
                .password("12312123").gender(Gender.MALE).birthDate(LocalDateTime.now()).build();

        StepVerifier
                .create(this.userPersistenceMDB.create(user))
                .expectNextMatches(saveUser -> {
                    assertEquals("Diego22",saveUser.getFirstName());
                    return true;
                })
                .expectComplete()
                .verify();
    }
}
