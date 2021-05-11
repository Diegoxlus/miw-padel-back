package miw_padel_back.infraestructure.api.dtos;

import lombok.*;
import miw_padel_back.domain.models.Gender;
import miw_padel_back.domain.models.Role;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserRegisterDto  {
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
    private LocalDateTime birthDate;
}
