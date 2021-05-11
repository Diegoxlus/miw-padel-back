package miw_padel_back.infraestructure.api.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserLoginDto {
    @NonNull
    private String email;
    @NonNull
    private String password;
}
