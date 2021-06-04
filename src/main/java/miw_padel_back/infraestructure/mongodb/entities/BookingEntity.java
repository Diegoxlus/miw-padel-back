package miw_padel_back.infraestructure.mongodb.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import miw_padel_back.domain.models.Booking;
import miw_padel_back.infraestructure.api.dtos.BookingDto;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Document(collection = "bookings")
public class BookingEntity {
    @Id
    private String id;
    @DBRef
    private UserEntity user;
    @DBRef
    private PaddleCourtEntity paddleCourt;
    private LocalDate date;
    private String timeRange;

    public BookingEntity(BookingDto bookingDto) {
        this.date = bookingDto.getDate();
        this.timeRange = bookingDto.getTimeRange();
    }


    public Booking toBooking() {
        var booking = new Booking();
        BeanUtils.copyProperties(this, booking);
        booking.setUser(this.user.toUser());
        booking.setPaddleCourt(this.paddleCourt.toPaddleCourt());
        return booking;
    }

    public BookingDto toBookingDto() {
        var bookingDto = new BookingDto();
        bookingDto.setId(this.getId());
        bookingDto.setDate(this.getDate());
        bookingDto.setPaddleCourtName(this.paddleCourt.getName());
        bookingDto.setTimeRange(this.getTimeRange());
        bookingDto.setEmail(this.getUser().getEmail());
        return bookingDto;
    }
}
