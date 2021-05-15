package miw_padel_back.infraestructure.mongodb.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import miw_padel_back.domain.models.PaddleCourt;
import miw_padel_back.domain.models.PaddleCourtType;
import miw_padel_back.infraestructure.api.dtos.PaddleCourtAvailabilityDto;
import miw_padel_back.infraestructure.mongodb.daos.synchronous.PaddleCourtDao;
import org.reactivestreams.Publisher;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Document(collection = "courts")
public class PaddleCourtEntity {
    @Id
    private String id;
    @Indexed(unique = true)
    private String name;
    private PaddleCourtType paddleCourtType;
    private List<String> startTimes;
    private List<String> endTimes;
    private boolean disabled;

    public PaddleCourt toPaddleCourt() {
        var paddleCourt = new PaddleCourt();
        BeanUtils.copyProperties(this,paddleCourt);
        return paddleCourt;
    }

}
