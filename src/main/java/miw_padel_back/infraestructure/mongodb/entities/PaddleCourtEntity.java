package miw_padel_back.infraestructure.mongodb.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import miw_padel_back.domain.models.PaddleCourtType;
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
@Document(collection = "PaddelCourts")
public class PaddleCourtEntity {
    @Indexed(unique = true)
    private String name;
    private PaddleCourtType paddleCourtType;
    private List<String> startTimes;
    private List<String> endTimes;
    private boolean disabled;
}
