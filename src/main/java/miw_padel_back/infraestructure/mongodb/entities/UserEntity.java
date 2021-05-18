package miw_padel_back.infraestructure.mongodb.entities;

import lombok.*;
import miw_padel_back.domain.models.Gender;
import miw_padel_back.domain.models.Role;
import miw_padel_back.domain.models.User;
import miw_padel_back.infraestructure.api.dtos.UserRegisterDto;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Document(collection = "users")
public class UserEntity {
    @Id
    private String id;
    @NonNull
    private String firstName;
    @NonNull
    private String familyName;
    @NonNull
    @Indexed(unique = true)
    @Email
    private String email;
    @NonNull
    private String password;
    @Transient
    private String matchingPassword;
    @NonNull
    private List<Role> roles;
    @NonNull
    private Gender gender;
    @NonNull
    private Boolean enabled;
    @NonNull
    private LocalDate birthDate;

    public User toUser() {
        var user = new User();
        BeanUtils.copyProperties(this, user);
        return user;
    }

    public UserRegisterDto toUserRegisterDtoWithoutPassword() {
        var user = new UserRegisterDto();
        BeanUtils.copyProperties(this, user);
        user.setPassword("");
        return user;
    }
}

