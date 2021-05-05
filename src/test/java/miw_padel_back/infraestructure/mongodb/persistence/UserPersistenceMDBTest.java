package miw_padel_back.infraestructure.mongodb.persistence;

import miw_padel_back.TestConfig;
import miw_padel_back.domain.model.Gender;
import miw_padel_back.domain.model.Role;
import miw_padel_back.domain.model.User;
import miw_padel_back.infraestructure.mongodb.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestConfig
public class UserPersistenceMDBTest {

    @Autowired
    private UserPersistenceMDB userPersistenceMDB;

    @Test
    void testGivenUserWhenCreateThenReturnUser() {
        List<Role> roleList = new ArrayList<>();
        roleList.add(Role.ADMIN);
        roleList.add(Role.PLAYER);
        User user = User.builder().firstName("aaaa").familyName("aaaa").email("aaaa@gmail.com")
                .password("aaaa").gender(Gender.MALE).roles(roleList).birthDate(LocalDateTime.now()).build();

        StepVerifier
                .create(this.userPersistenceMDB.create(user))
                .expectNextMatches(saveUser -> {
                    assertEquals("aaaa",saveUser.getFirstName());
                    return true;
                })
                .expectComplete()
                .verify();
    }
}
