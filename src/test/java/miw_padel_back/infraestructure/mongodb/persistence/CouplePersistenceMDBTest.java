package miw_padel_back.infraestructure.mongodb.persistence;

import miw_padel_back.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestConfig
public class CouplePersistenceMDBTest {

    @Autowired
    CouplePersistenceMDB couplePersistenceMDB;

    @Test
    void testGivenEmailWhenReadAllThenReturnPlayerCouples(){
        StepVerifier
                .create(this.couplePersistenceMDB.readPlayerCouples("captain@player.com"))
                .expectNextMatches(couple -> {
                    assertEquals("captain@player.com", couple.getCaptain().getEmail());
                    assertEquals("notcaptain@player.com", couple.getPlayer().getEmail());
                    return true;
                })
                .verifyComplete();

    }
}
