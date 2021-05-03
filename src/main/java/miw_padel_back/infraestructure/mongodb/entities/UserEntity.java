package miw_padel_back.infraestructure.mongodb.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import miw_padel_back.configuration.PasswordMatches;
import miw_padel_back.domain.model.Gender;
import miw_padel_back.domain.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.sql.Date;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Document(collection = "users")
@PasswordMatches
public class UserEntity {
    @Id
    private String id;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String familyName;
    @Indexed(unique=true)
    @Email
    private String email;
    @NotEmpty
    private String password;
    private String matchingPassword;
    @NotEmpty
    private Gender gender;
    @NotEmpty
    private LocalDateTime birthDate;

    public UserEntity(User user){
        BeanUtils.copyProperties(user,this);
    }

    public User toUser(){
        User user = new User();
        BeanUtils.copyProperties(this,user);
        return user;
    }
}

