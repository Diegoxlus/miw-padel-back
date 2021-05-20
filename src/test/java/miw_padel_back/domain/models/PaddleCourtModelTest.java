package miw_padel_back.domain.models;

import miw_padel_back.TestConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestConfig
class PaddleCourtModelTest {

    @Test
    void testGivenCorrectHoursWhenCheckThenReturnTrue() {
        var paddleCourt = PaddleCourt.builder()
                .name("CORRECT PADDLE COURT")
                .disabled(false)
                .startTime("09:00").startTime("10:30").startTime("12:00").startTime("13:30").startTime("15:00").startTime("16:30")
                .endTime("10:30").endTime("12:00").endTime("13:30").endTime("15:00").endTime("16:30").endTime("18:00")
                .build();
        assertTrue(paddleCourt.checkTimes());
    }

    @Test
    void testGivenEqualsStartTimeWhenCheckThenReturnFalse() {
        var paddleCourt = PaddleCourt.builder()
                .name("INCORRECT PADDLE COURT")
                .disabled(false)
                .startTime("09:00").startTime("09:00").startTime("12:00").startTime("13:30").startTime("15:00").startTime("16:30")
                .endTime("10:30").endTime("12:00").endTime("13:30").endTime("15:00").endTime("16:30").endTime("18:00")
                .build();
        assertFalse(paddleCourt.checkTimes());
    }

    @Test
    void testGivenIncorrectSequenceStartTimeWhenCheckThenReturnFalse() {
        var paddleCourt = PaddleCourt.builder()
                .name("INCORRECT PADDLE COURT")
                .disabled(false)
                .startTime("09:00").startTime("08:00").startTime("12:00").startTime("13:30").startTime("15:00").startTime("16:30")
                .endTime("10:30").endTime("12:00").endTime("13:30").endTime("15:00").endTime("16:30").endTime("18:00")
                .build();
        assertFalse(paddleCourt.checkTimes());
    }

    @Test
    void testGivenStartTimeMinorEndTimeWhenCheckThenReturnFalse() {
        var paddleCourt = PaddleCourt.builder()
                .name("INCORRECT PADDLE COURT")
                .disabled(false)
                .startTime("09:00").startTime("10:10").startTime("12:00").startTime("13:30").startTime("15:00").startTime("16:30")
                .endTime("10:30").endTime("12:00").endTime("13:30").endTime("15:00").endTime("16:30").endTime("18:00")
                .build();
        assertFalse(paddleCourt.checkTimes());
    }
}
