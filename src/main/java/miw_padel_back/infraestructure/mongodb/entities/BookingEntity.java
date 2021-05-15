package miw_padel_back.infraestructure.mongodb.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import miw_padel_back.domain.models.PaddleCourt;
import miw_padel_back.domain.models.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Document(collection = "bookings")
public class BookingEntity {
    @Id
    private String id;
    @DBRef
    private UserEntity user;
    @DBRef
    private PaddleCourtEntity paddleCourt;
    private Date date;
    private String timeRange;


}
