package miw_padel_back.infraestructure.mongodb.persistence;

import miw_padel_back.TestConfig;
import miw_padel_back.domain.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserPersistenceMDBIT {
    @Autowired
    private UserPersistenceMDB userPersistenceMDB;

    @Test
    void testGivenUserWhenCreateThenReturnUser() {
        User user = User.builder()
                .address("aa")
                .dni("123123")
                .email("aa")
                .mobile("123123123")
                .firstName("Diego")
                .familyName("LUS")
                .build();

        StepVerifier
                .create(this.userPersistenceMDB.create(user))
                .expectNextMatches(saveUser -> {
                    assertEquals("Diego",saveUser.getFirstName());
                    return true;
                })
                .expectComplete()
                .verify();
    }
}
