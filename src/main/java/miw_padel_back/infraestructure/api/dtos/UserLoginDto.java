package miw_padel_back.infraestructure.api.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class UserLoginDto {
    @NonNull
    private String email;
    @NonNull
    private String password;
}
