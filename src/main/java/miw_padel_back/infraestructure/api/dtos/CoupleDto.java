package miw_padel_back.infraestructure.api.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import miw_padel_back.domain.models.CoupleState;
import miw_padel_back.domain.models.Gender;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoupleDto {
    String id;
    String captainEmail;
    String captainName;
    String playerEmail;
    String playerName;
    CoupleState coupleState;
    Gender gender;
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "es_ES")
    LocalDate creationDate;
}
