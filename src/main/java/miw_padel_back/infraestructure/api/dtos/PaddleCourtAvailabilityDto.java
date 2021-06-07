package miw_padel_back.infraestructure.api.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class PaddleCourtAvailabilityDto {
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "es_ES")
    private LocalDate date;
    @Singular("availabilityHour")
    private Map<String, Boolean> availabilityHours = new TreeMap<>();
}
