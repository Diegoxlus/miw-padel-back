package miw_padel_back.infraestructure.mongodb.entities;

import lombok.*;
import miw_padel_back.configuration.PasswordMatches;
import miw_padel_back.domain.model.Gender;
import miw_padel_back.domain.model.Role;
import miw_padel_back.domain.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Document(collection = "users")
public class UserEntity {
    @Id
    private String id;
    @NotNull
    private String firstName;
    @NotNull
    private String familyName;
    @Indexed(unique = true)
    @Email
    private String email;
    @NotNull
    private String password;
    @Transient
    private String matchingPassword;
    private List<Role> role;
    @NonNull
    private Gender gender;
    @NotNull
    private Boolean enabled;
    @NotNull
    private LocalDateTime birthDate;

    public UserEntity(User user) {
        BeanUtils.copyProperties(user, this);
    }

    public User toUser() {
        User user = new User();
        BeanUtils.copyProperties(this, user);
        return user;
    }
}

