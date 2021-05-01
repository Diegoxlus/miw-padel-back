package miw_padel_back.infraestructure.mongodb.daos;

import miw_padel_back.domain.model.Gender;
import miw_padel_back.domain.model.User;
import miw_padel_back.infraestructure.mongodb.entities.UserEntity;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DatabaseSeederDev {
private UserReactive userReactive;

    @Autowired
    public DatabaseSeederDev(UserReactive userReactive) {
        this.userReactive = userReactive;
        this.seedDataBase();
    }

    private void seedDataBase(){
        this.userReactive.deleteAll();
        LogManager.getLogger(this.getClass()).warn("------- Initial Load from JAVA -----------");
        UserEntity[] userEntities = {
                UserEntity.builder().firstName("Diego").familyName("Lusqui").email("lusky1996@gmail.com")
                        .password("123123").build(),
                UserEntity.builder().firstName("Andrea").familyName("Álvarez").email("aamarinho@gmail.com")
                        .password("123123").gender(Gender.FEMALE).birthDate(LocalDateTime.now()).build()
        };
        this.userReactive.save(userEntities[0]);
        LogManager.getLogger(this.getClass()).warn("------- Finish Load from JAVA -----------");

    }
}
