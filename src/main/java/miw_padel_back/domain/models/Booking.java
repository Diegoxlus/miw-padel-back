package miw_padel_back.domain.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Booking {
    private User user;
    private PaddleCourt paddleCourt;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String timeRange;
}
