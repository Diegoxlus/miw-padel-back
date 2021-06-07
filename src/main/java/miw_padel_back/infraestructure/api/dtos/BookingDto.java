package miw_padel_back.infraestructure.api.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String id;
    private String email;
    private String paddleCourtName;
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "es_ES")
    private LocalDate date;
    private String timeRange;
}
