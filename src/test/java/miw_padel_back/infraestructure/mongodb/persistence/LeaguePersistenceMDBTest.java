package miw_padel_back.infraestructure.mongodb.persistence;

import miw_padel_back.TestConfig;
import miw_padel_back.domain.exceptions.NotFoundException;
import miw_padel_back.domain.models.Gender;
import miw_padel_back.domain.models.League;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LeaguePersistenceMDBTest {

    @Autowired
    LeaguePersistenceMDB leaguePersistenceMDB;

    static AtomicReference<String> atomicReference;

    @BeforeAll
    static void setUp(){
        atomicReference = new AtomicReference<>("NULL");
    }

    @Test
    @Order(1)
    void testWhenReadAllThenReturnLeaguesDto(){
        StepVerifier
                .create(this.leaguePersistenceMDB.readAll())
                .expectNextMatches(leagueDto -> {
                    assertEquals(Gender.MIXED,leagueDto.getGender());
                    assertFalse(leagueDto.getCouples().isEmpty());
                    return true;
                })
                .expectNextMatches(leagueDto -> {
                    assertEquals(Gender.MALE,leagueDto.getGender());
                    assertTrue(leagueDto.getCouples().isEmpty());
                    return true;
                })
                .expectNextMatches(leagueDto -> {
                    assertEquals(Gender.FEMALE,leagueDto.getGender());
                    assertTrue(leagueDto.getCouples().isEmpty());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    @Order(2)
    void testWhenCreateThenReturnLeagueDto(){
        League league = League.builder()
                .name("NEW LEAGUE")
                .gender(Gender.MIXED)
                .startDate(LocalDate.EPOCH)
                .endDate(LocalDate.EPOCH.plusDays(1))
                .maxCouples(10)
                .build();

        StepVerifier
                .create(this.leaguePersistenceMDB.create(league))
                .expectNextMatches(leagueDto -> {
                    atomicReference.set(leagueDto.getId());
                    assertEquals(league.getName(),leagueDto.getName());
                    assertEquals(league.getGender(),leagueDto.getGender());
                    assertEquals(league.getStartDate(),leagueDto.getStartDate());
                    assertEquals(league.getEndDate(),leagueDto.getEndDate());
                    assertTrue(leagueDto.getCouples().isEmpty());

                    return true;
                })
                .verifyComplete();
    }

    @Test
    @Order(3)
    void testGivenIdWhenDeleteThenOkVoid(){
        StepVerifier
                .create(this.leaguePersistenceMDB.delete(atomicReference.get()))
                .verifyComplete();
    }

    @Test
    void testGivenInvalidIdWhenDeleteThenReturnNotFound(){
        StepVerifier
                .create(this.leaguePersistenceMDB.delete("invalidID"))
                .expectErrorMatches(throwable -> throwable instanceof NotFoundException &&
                        throwable.getMessage().equals("Not Found Exception: League with this id not exists"))
                .verify();
    }
}
