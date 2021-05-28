package miw_padel_back.infraestructure.mongodb.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import miw_padel_back.domain.models.Couple;
import miw_padel_back.domain.models.CoupleState;
import miw_padel_back.domain.models.Gender;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

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

    public Couple toCouple() {
        var couple = new Couple();
        couple.setId(this.id);
        couple.setCaptain(this.captain.toUser());
        couple.setPlayer(this.player.toUser());
        couple.setCoupleState(this.coupleState);
        couple.setGender(this.gender);

        return couple;
    }
}
