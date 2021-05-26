package miw_padel_back.infraestructure.mongodb.persistence;

import miw_padel_back.TestConfig;
import miw_padel_back.domain.exceptions.BadRequestException;
import miw_padel_back.domain.exceptions.ConflictException;
import miw_padel_back.domain.exceptions.NotFoundException;
import miw_padel_back.domain.models.PaddleCourt;
import miw_padel_back.domain.models.PaddleCourtType;
import miw_padel_back.infraestructure.api.dtos.PaddleCourtAvailabilityDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;
import java.time.LocalDate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@TestConfig
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PaddleCourtMDBTest {
    private static final String NAME = "NEW COURT";

    PaddleCourt paddleCourt = PaddleCourt.builder()
            .name(NAME)
                .paddleCourtType(PaddleCourtType.INDOOR)
                .disabled(false)
                .startTime("09:00").startTime("10:30").startTime("12:00").startTime("13:30").startTime("15:00").startTime("16:30")
                .endTime("10:30").endTime("12:00").endTime("13:30").endTime("15:00").endTime("16:30").endTime("18:00")
                .build();

    PaddleCourtAvailabilityDto paddleCourtAvailabilityDtoPC1 = PaddleCourtAvailabilityDto.builder().name("PC 1").date(LocalDate.EPOCH)
            .availabilityHour("10:00 - 12:00",false)
            .availabilityHour("12:00 - 14:00",true)
            .build();

    PaddleCourtAvailabilityDto paddleCourtAvailabilityDtoPC2 = PaddleCourtAvailabilityDto.builder().name("PC 2").date(LocalDate.EPOCH)
            .availabilityHour("10:00 - 12:00",true)
            .availabilityHour("12:00 - 14:00",false)
            .availabilityHour("14:00 - 16:00",true)
            .build();

    PaddleCourtAvailabilityDto paddleCourtAvailabilityDtoPC3 = PaddleCourtAvailabilityDto.builder().name("PC 3").date(LocalDate.EPOCH)
            .availabilityHour("10:00 - 12:00",true)
            .availabilityHour("12:00 - 14:00",true)
            .availabilityHour("14:00 - 16:00",true)
            .build();

    PaddleCourtAvailabilityDto paddleCourtAvailabilityDtoPC4 = PaddleCourtAvailabilityDto.builder().name("PC 4").date(LocalDate.EPOCH)
            .availabilityHour("10:00 - 12:00",true)
            .availabilityHour("12:00 - 14:00",true)
            .availabilityHour("14:00 - 16:00",true)
            .build();

    @Autowired
    private PaddleCourtPersistenceMDB paddleCourtPersistenceMDB;


    @Test
    @Order(1)
    void testGivenPaddleCourtWhenCreateThenReturnPaddleCourt(){

        StepVerifier
                .create(this.paddleCourtPersistenceMDB.create(paddleCourt))
                .expectNextMatches(savePaddleCourt -> {
                    assertEquals(NAME,savePaddleCourt.getName());
                    assertFalse(savePaddleCourt.isDisabled());
                    assertEquals(savePaddleCourt.getStartTimes(), paddleCourt.getStartTimes());
                    assertEquals(savePaddleCourt.getEndTimes(), paddleCourt.getEndTimes());
                    return true;
                })
                .expectComplete()
                .verify();
    }

    @Test
    @Order(2)
    void testGivenPaddleCourtNameWhenReadByNameThenReturnPaddleCourt(){
        StepVerifier
                .create(this.paddleCourtPersistenceMDB.readByName(NAME))
                .expectNextMatches(savePaddleCourt -> {
                    assertEquals(NAME,savePaddleCourt.getName());
                    assertFalse(savePaddleCourt.isDisabled());
                    assertEquals(savePaddleCourt.getStartTimes(), paddleCourt.getStartTimes());
                    assertEquals(savePaddleCourt.getEndTimes(), paddleCourt.getEndTimes());
                    return true;
                })
                .expectComplete()
                .verify();
    }

    @Test
    @Order(3)
    void testGivenExistentPaddleCourtWhenCreateThenReturnConflictException(){
        StepVerifier
                .create(this.paddleCourtPersistenceMDB.create(paddleCourt))
                .expectErrorMatches(throwable -> throwable instanceof ConflictException &&
                        throwable.getMessage().equals("Conflict Exception: Name "+ paddleCourt.getName()+ "already exists"))
                .verify();
    }

    @Test
    @Order(4)
    void testGivenPaddleCourtNameWhenDeleteThenReturnVoid(){
        StepVerifier
                .create(this.paddleCourtPersistenceMDB.deleteByName(NAME))
                .expectNext()
                .expectComplete()
                .verify();
    }

    @Test
    @Order(5)
    void testGivenPaddleWithoutOneEndTimeCourtWhenCreateThenReturnBadRequest() {
        PaddleCourt paddleCourt = PaddleCourt.builder()
                .name(NAME)
                .disabled(false)
                .startTime("09:00").startTime("10:30").startTime("12:00").startTime("13:30").startTime("15:00").startTime("16:30")
                .endTime("10:30").endTime("12:00").endTime("13:30").endTime("15:00").endTime("16:30")
                .build();

        StepVerifier
                .create(this.paddleCourtPersistenceMDB.create(paddleCourt))
                .expectErrorMatches(throwable -> throwable instanceof BadRequestException &&
                        throwable.getMessage().equals("Bad Request Exception: Check hours for this paddle court"))
                .verify();
    }


    @Test
    void testGivenNotExistentPaddleCourtNameWhenReadByNameThenReturnBadRequest(){
        var invalidName = "INVALID_NAME";
        StepVerifier
                .create(this.paddleCourtPersistenceMDB.readByName(invalidName))
                .expectErrorMatches(throwable -> throwable instanceof NotFoundException &&
                        throwable.getMessage().equals("Not Found Exception: Non exists paddle court with name "+ invalidName))
                .verify();
    }



    @Test
    void testGivenPaddleCourtNameAndDateWhenReadAvailabilityThenReturnPaddleCourtAvailabilityDto() {

        StepVerifier
                .create(this.paddleCourtPersistenceMDB.readAvailabilityByNameAndDate("PC 1", LocalDate.EPOCH))
                .expectNextMatches(paddleCourtAvailabilityDto -> {
                    assertEquals(paddleCourtAvailabilityDto,this.paddleCourtAvailabilityDtoPC1);
                    return true;
                })
                .expectComplete()
                .verify();
    }

    @Test
    void testGivenDateWhenReadAvailabilityByDateThenReturnListPaddleCourtAvailabilityDto() {

        StepVerifier
                .create(this.paddleCourtPersistenceMDB.readAvailabilityByDate(LocalDate.EPOCH))
                .expectNextMatches(paddleCourtAvailabilityDto -> {
                    assertEqualsAny(paddleCourtAvailabilityDto);
                    return true;
                })
                .expectNextMatches(paddleCourtAvailabilityDto -> {
                    assertEqualsAny(paddleCourtAvailabilityDto);
                    return true;
                })
                .expectNextMatches(paddleCourtAvailabilityDto -> {
                    assertEqualsAny(paddleCourtAvailabilityDto);
                    return true;
                })
                .expectNextMatches(paddleCourtAvailabilityDto -> {
                    assertEqualsAny(paddleCourtAvailabilityDto);
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void testGivenIncorrectPaddleCourtNameAndDateWhenReadAvailabilityThenReturnNotFound() {
        var invalidName = "INVALID_NAME";
        StepVerifier
                .create(this.paddleCourtPersistenceMDB.readAvailabilityByNameAndDate(invalidName, LocalDate.EPOCH))
                .expectErrorMatches(throwable -> throwable instanceof NotFoundException &&
                        throwable.getMessage().equals("Not Found Exception: Non exists paddle court with name " + invalidName))
                .verify();
    }

    private void assertEqualsAny(PaddleCourtAvailabilityDto paddleCourtAvailabilityDto) {
        assertThat(paddleCourtAvailabilityDto, anyOf(is(paddleCourtAvailabilityDtoPC1)
                , is(paddleCourtAvailabilityDtoPC2)
                , is(paddleCourtAvailabilityDtoPC3)
                , is(paddleCourtAvailabilityDtoPC4)));
    }

}
