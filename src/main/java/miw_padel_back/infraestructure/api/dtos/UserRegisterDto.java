package miw_padel_back.infraestructure.api.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import miw_padel_back.domain.models.Gender;
import miw_padel_back.domain.models.Role;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserRegisterDto  {
    private String firstName;
    private String familyName;
    private String email;
    private List<Role> roles;
    private String password;
    private Gender gender;
    private boolean enabled;
    private LocalDateTime birthDate;
}
