package miw_padel_back.infraestructure.mongodb.persistence;

import miw_padel_back.TestConfig;
import miw_padel_back.domain.exceptions.BadRequestException;
import miw_padel_back.domain.exceptions.ConflictException;
import miw_padel_back.domain.exceptions.NotFoundException;
import miw_padel_back.domain.models.Gender;
import miw_padel_back.domain.models.PaddleCourt;
import miw_padel_back.domain.models.PaddleCourtType;
import miw_padel_back.domain.models.Role;
import miw_padel_back.infraestructure.api.dtos.PaddleCourtAvailabilityDto;
import miw_padel_back.infraestructure.api.dtos.UserLoginDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PaddleCourtMDBTest {
    private static final String NAME = "COURT 1";
    private static final String PADDLE_COURT_TYPE = PaddleCourtType.INDOOR.name();
    private static final Boolean DISABLED = false;

    private static PaddleCourt paddleCourt;

    @Autowired
    private PaddleCourtPersistenceMDB paddleCourtPersistenceMDB;

    @BeforeAll
    public static void setUp(){
        paddleCourt = PaddleCourt.builder()
                .name(NAME)
                .disabled(DISABLED)
                .startTime("09:00").startTime("10:30").startTime("12:00").startTime("13:30").startTime("15:00").startTime("16:30")
                .endTime("10:30").endTime("12:00").endTime("13:30").endTime("15:00").endTime("16:30").endTime("18:00")
                .build();
    }

    @Test
    @Order(1)
    void testGivenPaddleCourtWhenCreateThenReturnPaddleCourt(){
        StepVerifier
                .create(this.paddleCourtPersistenceMDB.create(paddleCourt))
                .expectNextMatches(savePaddleCourt -> {
                    assertEquals(NAME,savePaddleCourt.getName());
                    assertEquals(DISABLED,savePaddleCourt.isDisabled());
                    assertEquals(savePaddleCourt.getStartTimes(), paddleCourt.getStartTimes());
                    assertEquals(savePaddleCourt.getEndTimes(), paddleCourt.getEndTimes());
                    return true;
                })
                .expectComplete()
                .verify();
    }

    @Test
    @Order(2)
    void testGivenExistentPaddleCourtWhenCreateThenReturnConflictException(){
        StepVerifier
                .create(this.paddleCourtPersistenceMDB.create(paddleCourt))
                .expectErrorMatches(throwable -> throwable instanceof ConflictException &&
                        throwable.getMessage().equals("Conflict Exception: Name "+ paddleCourt.getName()+ "already exists"))
                .verify();
    }

    @Test
    void testGivenPaddleWithoutOneEndTimeCourtWhenCreateThenReturnBadRequest() {
        PaddleCourt paddleCourt = PaddleCourt.builder()
                .name(NAME)
                .disabled(DISABLED)
                .startTime("09:00").startTime("10:30").startTime("12:00").startTime("13:30").startTime("15:00").startTime("16:30")
                .endTime("10:30").endTime("12:00").endTime("13:30").endTime("15:00").endTime("16:30")
                .build();

        StepVerifier
                .create(this.paddleCourtPersistenceMDB.create(paddleCourt))
                .expectErrorMatches(throwable -> throwable instanceof BadRequestException &&
                        throwable.getMessage().equals("BadRequest Exception: Incorrect times"))
                .verify();
    }

    @Test
    @Order(3)
    void testGivenPaddleCourtNameWhenReadByNameThenReturnPaddleCourt(){
        StepVerifier
                .create(this.paddleCourtPersistenceMDB.readByName(NAME))
                .expectNextMatches(savePaddleCourt -> {
                    assertEquals(NAME,savePaddleCourt.getName());
                    assertEquals(DISABLED,savePaddleCourt.isDisabled());
                    assertEquals(savePaddleCourt.getStartTimes(), paddleCourt.getStartTimes());
                    assertEquals(savePaddleCourt.getEndTimes(), paddleCourt.getEndTimes());
                    return true;
                })
                .expectComplete()
                .verify();
    }

    @Test
    void testGivenNotExistentPaddleCourtNameWhenReadByNameThenReturnBadRequest(){
        var invalidName = "INVALID_NAME";
        StepVerifier
                .create(this.paddleCourtPersistenceMDB.readByName(invalidName))
                .expectErrorMatches(throwable -> throwable instanceof BadRequestException &&
                        throwable.getMessage().equals("BadRequest Exception: Incorrect name: "+ invalidName))
                .verify();
    }

    @Test
    void testGivenPaddleCourtNameAndDateWhenReadAvailabilityThenReturnPaddleCourtAvailabilityDto(){
        StepVerifier
                .create(this.paddleCourtPersistenceMDB.readAvailabilityByNameAndDate("PC 1", LocalDate.EPOCH))
                .expectNextMatches(paddleCourtAvailabilityDto -> {
                    assertEquals("PC 1",paddleCourtAvailabilityDto.getName());
                    assertEquals(false,paddleCourtAvailabilityDto.getAvailabilityHours().get("10:00 - 12:00"));
                    assertEquals(true,paddleCourtAvailabilityDto.getAvailabilityHours().get("12:00 - 14:00"));
                    assertEquals(LocalDate.EPOCH, paddleCourtAvailabilityDto.getDate());
                    return true;
                })
                .expectComplete()
                .verify();
    }

    @Test
    void testGivenDateWhenReadAvailabilityByDateThenReturnListPaddleCourtAvailabilityDto(){
        StepVerifier
                .create(this.paddleCourtPersistenceMDB.readAvailabilityByDate(LocalDate.EPOCH))
                .recordWith(ArrayList::new)
                .thenConsumeWhile(x -> true)
                .expectRecordedMatches(paddleCourtAvailabilityDtos -> {
                    var paddleCourtAvailabilityDtoList = new ArrayList<>(paddleCourtAvailabilityDtos);
                    this.verifyContainsPaddleCourtNameInList(paddleCourtAvailabilityDtoList,"PC 1");
                    this.verifyContainsPaddleCourtNameInList(paddleCourtAvailabilityDtoList,"PC 2");
                    return true;

                })
        .verifyComplete();
    }

    private void verifyContainsPaddleCourtNameInList(List<PaddleCourtAvailabilityDto> paddleCourtAvailabilityDtoList, String reference){
        assertNotNull(paddleCourtAvailabilityDtoList
                .stream()
                .filter(paddleCourtAvailabilityDto -> paddleCourtAvailabilityDto.getName().equals(reference))
                .findAny()
                .orElse(null));
    }

    @Test
    void testGivenIncorrectPaddleCourtNameAndDateWhenReadAvailabilityThenReturnNotFound(){
        var invalidName = "INVALID_NAME";
        StepVerifier
                .create(this.paddleCourtPersistenceMDB.readAvailabilityByNameAndDate(invalidName,LocalDate.EPOCH))
                .expectErrorMatches(throwable -> throwable instanceof NotFoundException &&
                        throwable.getMessage().equals("Not Found Exception: Non existent paddle court with name: " + invalidName))
                .verify();
    }

}
