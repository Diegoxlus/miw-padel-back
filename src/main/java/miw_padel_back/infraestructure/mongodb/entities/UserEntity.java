package miw_padel_back.infraestructure.mongodb.entities;

import com.fasterxml.jackson.databind.util.BeanUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import miw_padel_back.domain.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Document
public class UserEntity {
    @Id
    private String id;
    private String mobile;
    private String firstName;
    private String familyName;
    @Indexed(unique=true)
    private String email;
    @Indexed(unique=true)
    private String dni;
    private String address;

    public UserEntity(User user){
        BeanUtils.copyProperties(user,this);
    }

    public User toUser(){
        User user = new User();
        BeanUtils.copyProperties(this,user);
        return user;
    }
}

