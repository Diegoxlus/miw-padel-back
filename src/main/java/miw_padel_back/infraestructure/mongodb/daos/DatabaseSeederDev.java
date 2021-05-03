package miw_padel_back.infraestructure.mongodb.daos;

import miw_padel_back.domain.model.Gender;
import miw_padel_back.infraestructure.mongodb.daos.reactive.UserReactive;
import miw_padel_back.infraestructure.mongodb.daos.synchronous.UserDao;
import miw_padel_back.infraestructure.mongodb.entities.UserEntity;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DatabaseSeederDev {
private UserDao userDao;

    @Autowired
    public DatabaseSeederDev(UserDao userDao) {
        this.userDao = userDao;
        this.seedDataBase();
    }

    private void seedDataBase(){
        this.userDao.deleteAll();
        LogManager.getLogger(this.getClass()).warn("------- Initial Load from JAVA -----------");
        UserEntity[] userEntities = {
                UserEntity.builder().firstName("Diego").familyName("Lusqui").email("lusky1996@gmail.com")
                        .password("123123").gender(Gender.MALE).birthDate(LocalDateTime.now()).build(),
                UserEntity.builder().firstName("Andrea").familyName("Álvarez").email("aamarinho@gmail.com")
                        .password("123123").gender(Gender.FEMALE).birthDate(LocalDateTime.now()).build()
        };
        this.userDao.saveAll(List.of(userEntities));
        LogManager.getLogger(this.getClass()).warn("------- Finish Load from JAVA -----------");

    }
}
