package miw_padel_back.infraestructure.mongodb.persistence;

import miw_padel_back.TestConfig;
import miw_padel_back.domain.exceptions.ForbiddenException;
import miw_padel_back.infraestructure.api.dtos.BookingDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestConfig
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookingPersistenceMDBTest {

    @Autowired
    private BookingPersistenceMDB bookingPersistenceMDB;

    static AtomicReference<String> atomicReference;

    @BeforeAll
    static void setUp(){
      atomicReference = new AtomicReference<>("NULL");
    }

    @Test
    @Order(1)
    void testGivenDateWhenReadByDateThenReturnBookings() {
        System.out.println(1);
        StepVerifier
                .create(this.bookingPersistenceMDB.readByDate(LocalDate.EPOCH))
                .expectNextMatches(bookingDto -> {
                    assertEquals(LocalDate.EPOCH, bookingDto.getDate());
                    assertEquals("admin@admin.com", bookingDto.getEmail());
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
    @Order(2)
    void testGivenBookingDtoWhenCreateThenReturnBookingDto(){
        System.out.println(2);
        BookingDto bookingDto = BookingDto.builder()
                .date(LocalDate.EPOCH)
                .email("player@player.com")
                .paddleCourtName("PC 3")
                .timeRange("10:00 - 12:00")
                .build();

        StepVerifier
                .create(this.bookingPersistenceMDB.create(bookingDto))
                .expectNextMatches(booking -> {
                    System.out.println(booking);
                    atomicReference.set(booking.getId());
                    System.out.println(atomicReference.get());
                    assertEquals("player@player.com",booking.getEmail());
                    return true;
                })
                .verifyComplete();
    }

    @Test
    @Order(3)
    void testGivenIdAndEmailWhenDeleteThenReturnForbidden(){
        StepVerifier
                .create(this.bookingPersistenceMDB.deleteMyBooking(atomicReference.get(),"forbidden@forbidden"))
                .expectErrorMatches(throwable -> throwable instanceof ForbiddenException &&
                        throwable.getMessage().equals("Forbidden Exception. Booking with id:" + atomicReference+ "is not yours"))
                .verify();
    }

}
