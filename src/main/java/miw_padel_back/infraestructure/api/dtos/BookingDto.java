package miw_padel_back.infraestructure.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class BookingDto {
    private String email;
    private String paddleCourtName;
    private LocalDate date;
    private String timeRange;
}
