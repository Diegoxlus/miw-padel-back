package miw_padel_back.infraestructure.mongodb.persistence;

import miw_padel_back.TestConfig;
import miw_padel_back.infraestructure.api.dtos.BookingDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestConfig
public class BookingPersistenceMDBTest {

    @Autowired
    private BookingPersistenceMDB bookingPersistenceMDB;

    @Test
    void testGivenDateWhenReadByDateThenReturnBookings() {
        StepVerifier
                .create(this.bookingPersistenceMDB.readByDate(LocalDate.EPOCH))
                .expectNextMatches(bookingDto -> {
                    assertEquals(LocalDate.EPOCH, bookingDto.getDate());
                    assertEquals("lusky1996@gmail.com", bookingDto.getEmail());
                    assertEquals("10:00 - 12:00", bookingDto.getTimeRange());
                    return true;
                })
                .expectNextMatches(bookingDto -> {
                    assertEquals(LocalDate.EPOCH, bookingDto.getDate());
                    //assertEquals("lusky1996@gmail.com",booking.getUser().getEmail());
                    assertEquals("12:00 - 14:00", bookingDto.getTimeRange());
                    return true;
                })
                .expectComplete()
                .verify();
    }

    @Test
    void test(){
        BookingDto bookingDto = BookingDto.builder()
                .date(LocalDate.EPOCH)
                .email("aamarinho@gmail.com")
                .paddleCourtName("PC 2")
                .timeRange("10:00 - 12:00")
                .build();

        StepVerifier
                .create(this.bookingPersistenceMDB.create(bookingDto))
                .expectNextMatches(booking -> {
                    assertEquals("aamarinho@gmail.com",booking.getUser().getEmail());
                    return true;
                })
                .verifyComplete();
    }

}
