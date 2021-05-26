package miw_padel_back.domain.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import miw_padel_back.domain.utils.StreamsUtils;
import miw_padel_back.infraestructure.api.dtos.PaddleCourtAvailabilityDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaddleCourt {
    private String id;
    private String name;
    private PaddleCourtType paddleCourtType;
    @Singular("startTime")
    private List<String> startTimes;
    @Singular("endTime")
    private List<String> endTimes;
    private boolean disabled;

    public Boolean checkTimes() {
        return checkFirstMinorSecondSequence(startTimes)
                && checkFirstMinorSecondSequence(endTimes)
                && checkStartEndTime();
    }

    private Boolean checkFirstMinorSecondSequence(List<String> times) {
        var correctTime = new AtomicBoolean(true);
        List<LocalTime> localTimes = new ArrayList<>();
        if (!times.isEmpty()) {
            times.forEach(startTime -> localTimes.add(LocalTime.parse(startTime)));
            localTimes.stream().reduce((first, second) -> {
                if (first.compareTo(second) >= 0) {
                    correctTime.set(false);
                }
                return second;
            });
        }
        return correctTime.get();
    }

    private Boolean checkFirstMinorOrEqualsSecondSequence(List<String> times) {
        var correctTime = new AtomicBoolean(true);
        List<LocalTime> localTimes = new ArrayList<>();
        if (!times.isEmpty()) {
            times.forEach(startTime -> localTimes.add(LocalTime.parse(startTime)));
            localTimes.stream().reduce((first, second) -> {
                if (first.compareTo(second) > 0) {
                    correctTime.set(false);
                }
                return second;
            });
        }
        return correctTime.get();
    }

    private Boolean checkStartEndTime() {
        if (this.startTimes.size() != this.endTimes.size()) {
            return false;
        }
        var mergeTimes = StreamsUtils.zip(startTimes.stream(), endTimes.stream(), Stream::of)
                .flatMap(Function.identity())
                .collect(Collectors.toList());

        return checkFirstMinorOrEqualsSecondSequence(mergeTimes);

    }

    public PaddleCourtAvailabilityDto createPaddleCourtAvailabilityDtoWithHours(LocalDate date) {
        var paddleCourtAvailabilityDto = new PaddleCourtAvailabilityDto();
        paddleCourtAvailabilityDto.setName(this.getName());
        paddleCourtAvailabilityDto.setDate(date);
        StreamsUtils.zip(startTimes.stream(), endTimes.stream(), (a, b) -> Stream.of(a + " - " + b)
        )
                .flatMap(Function.identity())
                .collect(Collectors.toList())
                .forEach(mergeTime -> paddleCourtAvailabilityDto.getAvailabilityHours().put(mergeTime, true));
        return paddleCourtAvailabilityDto;
    }
}
