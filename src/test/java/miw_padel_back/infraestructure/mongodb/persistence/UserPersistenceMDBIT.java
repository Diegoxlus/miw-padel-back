package miw_padel_back.infraestructure.mongodb.persistence;

import miw_padel_back.TestConfig;
import miw_padel_back.domain.model.User;
import miw_padel_back.infraestructure.mongodb.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestConfig
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
