package miw_padel_back.infraestructure.api.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import miw_padel_back.domain.models.Gender;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LeagueDto {
    private String id;
    private String name;
    private Gender gender;
    private List<CoupleDto> couples;
    private int maxCouples;
    private LocalDate startDate;
    private LocalDate endDate;
}

