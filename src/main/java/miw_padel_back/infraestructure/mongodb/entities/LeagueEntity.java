package miw_padel_back.infraestructure.mongodb.entities;

import lombok.*;
import miw_padel_back.domain.models.Gender;
import miw_padel_back.domain.models.League;
import miw_padel_back.infraestructure.api.dtos.LeagueDto;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @Singular
    private List<CoupleEntity> couples = new ArrayList<>();
    private int maxCouples;
    private LocalDate startDate;
    private LocalDate endDate;

    public LeagueEntity(League league) {
        this.name = league.getName();
        this.gender = league.getGender();
        this.maxCouples = league.getMaxCouples();
        this.startDate = league.getStartDate();
        this.endDate = league.getEndDate();
    }

    public LeagueDto toLeagueDto() {
        var leagueDto = new LeagueDto();
        leagueDto.setId(this.id);
        leagueDto.setName(this.name);
        leagueDto.setGender(this.gender);
        leagueDto.setCouples(couples.stream().
                map(CoupleEntity::toCoupleDto).
                collect(Collectors.toList()));
        leagueDto.setMaxCouples(maxCouples);
        leagueDto.setStartDate(startDate);
        leagueDto.setEndDate(endDate);

        return leagueDto;
    }
}
