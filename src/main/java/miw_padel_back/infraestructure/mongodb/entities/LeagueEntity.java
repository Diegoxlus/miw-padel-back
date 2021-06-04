package miw_padel_back.infraestructure.mongodb.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import miw_padel_back.domain.models.Couple;
import miw_padel_back.domain.models.Gender;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Document(collection = "leagues")
public class LeagueEntity {
    @Id
    private String id;
    private String name;
    private Gender gender;
    private List<CoupleEntity> couples = new ArrayList<>();
    private int maxCouples;
    private LocalDate startDate;
    private LocalDate endDate;
}
