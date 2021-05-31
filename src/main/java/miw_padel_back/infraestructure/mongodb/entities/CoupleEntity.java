package miw_padel_back.infraestructure.mongodb.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import miw_padel_back.domain.models.Couple;
import miw_padel_back.domain.models.CoupleState;
import miw_padel_back.domain.models.Gender;
import miw_padel_back.infraestructure.api.dtos.CoupleDto;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Document(collection = "couples")
public class CoupleEntity {
    @Id
    private String id;
    @DBRef
    private UserEntity captain;
    @DBRef
    private UserEntity player;
    private CoupleState coupleState;
    private Gender gender;
    private LocalDate creationDate;

    public Couple toCouple() {
        var couple = new Couple();
        couple.setId(this.id);
        couple.setCaptain(this.captain.toUser());
        couple.setPlayer(this.player.toUser());
        couple.setCoupleState(this.coupleState);
        couple.setGender(this.gender);
        return couple;
    }

    public CoupleDto toCoupleDto() {
        var couple = new CoupleDto();
        couple.setId(this.id);
        couple.setCaptainEmail(this.captain.getEmail());
        couple.setCaptainName(this.captain.getFirstName());
        couple.setPlayerEmail(this.player.getEmail());
        couple.setPlayerName(this.player.getFirstName());
        couple.setCoupleState(this.coupleState);
        couple.setGender(this.gender);
        return couple;
    }
}
