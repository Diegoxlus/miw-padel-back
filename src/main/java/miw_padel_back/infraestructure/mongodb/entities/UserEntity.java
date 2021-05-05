package miw_padel_back.infraestructure.mongodb.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @NonNull
    private String firstName;
    @NonNull
    private String familyName;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime birthDate;

    public UserEntity(User user) {
        BeanUtils.copyProperties(user, this);
    }

    public User toUser() {
        User user = new User();
        BeanUtils.copyProperties(this, user);
        return user;
    }

    public User toUserWithoutPassword() {
        User user = new User();
        BeanUtils.copyProperties(this, user);
        user.setPassword("");
        return user;
    }



}

