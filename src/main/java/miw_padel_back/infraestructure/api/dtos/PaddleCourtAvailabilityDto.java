package miw_padel_back.infraestructure.api.dtos;

import lombok.*;

import java.util.Date;
import java.util.HashMap;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class PaddleCourtAvailabilityDto {
    private String name;
    private Date date;
    private HashMap<String,Boolean> availabilityHours = new HashMap<>();

}
