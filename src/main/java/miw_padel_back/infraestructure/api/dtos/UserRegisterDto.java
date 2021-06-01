package miw_padel_back.infraestructure.api.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import miw_padel_back.domain.models.Gender;
import miw_padel_back.domain.models.Role;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserRegisterDto {

    @NonNull
    private String firstName;
    @NonNull
    private String familyName;
    @NonNull
    private String email;
    private List<Role> roles;
    @NonNull
    private String password;
    @NonNull
    private Gender gender;
    private boolean enabled;
    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "es_ES")
    private LocalDate birthDate;
    private String photo;
}
