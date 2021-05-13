package miw_padel_back.domain.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaddleCourt {
    private String name;
    private PaddleCourtType paddleCourtType;
    @Singular("startTime")
    private List<String> startTimes;
    @Singular("endTime")
    private List<String> endTimes;
    private boolean disabled;

    Boolean checkTimes(){
        return checkSequenceOfTime(startTimes) && checkSequenceOfTime(endTimes);
    }

    private Boolean checkSequenceOfTime(List<String> times) {
        var correctTime = new AtomicBoolean(true);
        List<LocalTime> localTimes = new ArrayList<>();
        if (!times.isEmpty()) {
            times.forEach(startTime -> localTimes.add(LocalTime.parse(startTime)));
            localTimes.stream().reduce((first, second) -> {
                if (first.compareTo(second)>=0) {
                    correctTime.set(false);
                }
                return second;
            });
        }
        return correctTime.get();
    }
}
