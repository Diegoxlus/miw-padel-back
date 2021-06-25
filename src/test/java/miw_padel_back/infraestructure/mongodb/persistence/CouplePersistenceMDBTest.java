package miw_padel_back.infraestructure.mongodb.persistence;

import miw_padel_back.TestConfig;
import miw_padel_back.domain.exceptions.ConflictException;
import miw_padel_back.domain.models.CoupleState;
import miw_padel_back.domain.models.Gender;
import miw_padel_back.infraestructure.api.dtos.EmailDto;
import miw_padel_back.infraestructure.api.dtos.IdDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestConfig
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CouplePersistenceMDBTest {

    @Autowired
    CouplePersistenceMDB couplePersistenceMDB;
    static AtomicReference<String> atomicReference;

    @BeforeAll
    static void setUp() {
        atomicReference = new AtomicReference<>("NULL");
    }


    @Test
    @Order(1)
    void testGivenEmailDtoAndPlayerEmailWhenCreateThenReturnCouple() {
        StepVerifier
                .create(this.couplePersistenceMDB.createCouplePetition("captain2@player.com", EmailDto.builder().email("player2@player.com").build()))
                .expectNextMatches(couple -> {
                    atomicReference.set(couple.getId());
                    assertEquals("captain2@player.com", couple.getCaptainEmail());
                    assertEquals("player2@player.com", couple.getPlayerEmail());
                    assertEquals(Gender.MIXED, couple.getGender());
                    assertEquals(CoupleState.PENDING, couple.getCoupleState());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    @Order(2)
    void testGivenEmailWhenReadAllThenReturnPlayerCouples() {
        StepVerifier
                .create(this.couplePersistenceMDB.readPlayerCouples("captain@player.com"))
                .expectNextMatches(couple -> {
                    assertEquals("captain@player.com", couple.getCaptainEmail());
                    assertEquals("notcaptain@player.com", couple.getPlayerEmail());
                    return true;
                })
                .verifyComplete();
    }
    @Test
    @Order(3)
    void testGivenEmailAndIdWhenDeleteThenReturnMonoVoid() {
        StepVerifier
                .create(this.couplePersistenceMDB.deleteCouplePetition("player2@player.com", atomicReference.get()))
                .verifyComplete();

        this.testGivenEmailDtoAndPlayerEmailWhenCreateThenReturnCouple();
    }

    @Test
    @Order(4)
    void testGivenEmailDtoAndPlayerEmailWhenAcceptCouplePetitionThenReturnCouple() {
        StepVerifier
                .create(this.couplePersistenceMDB.acceptCouplePetition("player2@player.com", IdDto.builder().id(atomicReference.get()).build()))
                .expectNextMatches(couple -> {
                    assertEquals("captain2@player.com", couple.getCaptainEmail());
                    assertEquals("player2@player.com", couple.getPlayerEmail());
                    assertEquals(Gender.MIXED, couple.getGender());
                    assertEquals(CoupleState.CONSOLIDATED, couple.getCoupleState());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    @Order(5)
    void testGivenIncorrectEmailAndIdWhenDeleteThenReturnConflictException() {
        StepVerifier
                .create(this.couplePersistenceMDB.deleteCouplePetition("invalid@player.com", atomicReference.get()))
                .expectErrorMatches(throwable -> throwable instanceof ConflictException &&
                        throwable.getMessage().equals("Conflict Exception: This couple petition isn´t yours"))
                .verify();
    }

    @Test
    @Order(6)
    void testGivenEmailAndIdWithConsolidatedStateWhenDeleteThenReturnConflictException() {
        StepVerifier
                .create(this.couplePersistenceMDB.deleteCouplePetition("player2@player.com", atomicReference.get()))
                .expectErrorMatches(throwable -> throwable instanceof ConflictException &&
                        throwable.getMessage().equals("Conflict Exception: This couple state isn´t pending"))
                .verify();
    }

    @Test
    @Order(7)
    void testGivenExistentCoupleWhenCreateThenReturnConflictException() {
        StepVerifier
                .create(this.couplePersistenceMDB.createCouplePetition("captain2@player.com", EmailDto.builder().email("player2@player.com").build()))
                .expectErrorMatches(throwable -> throwable instanceof ConflictException &&
                        throwable.getMessage().equals("Conflict Exception: Couple already exists"))
                .verify();
    }
}
