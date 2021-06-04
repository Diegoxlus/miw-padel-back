package miw_padel_back.domain.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class League {
    private String id;
    private String name;
    private Gender gender;
    private List<Couple> couples = new ArrayList<>();
    private int maxCouples;
    private LocalDate startDate;
    private LocalDate endDate;
}
