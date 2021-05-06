package miw_padel_back.infraestructure.mongodb.daos;

import miw_padel_back.configuration.security.PBKDF2Encoder;
import miw_padel_back.domain.model.Gender;
import miw_padel_back.domain.model.Role;
import miw_padel_back.infraestructure.mongodb.daos.synchronous.UserDao;
import miw_padel_back.infraestructure.mongodb.entities.UserEntity;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DatabaseSeederDev {
    private final UserDao userDao;
    private final PBKDF2Encoder passwordEncoder;

    @Autowired
    public DatabaseSeederDev(UserDao userDao, PBKDF2Encoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.seedDataBase();
    }

    private void seedDataBase() {
        List<Role> roles = new ArrayList<>();
        roles.add(Role.ADMIN);
        roles.add(Role.PLAYER);
        this.userDao.deleteAll();
        LogManager.getLogger(this.getClass()).warn("------- Initial Load from JAVA -----------");
        var userEntities = new UserEntity[] {
                UserEntity.builder().firstName("Diego").familyName("Lusqui").email("lusky1996@gmail.com")
                        .password("11111").matchingPassword("11111").gender(Gender.MALE).roles(roles.subList(0, 1)).enabled(true).birthDate(LocalDateTime.now()).build(),
                UserEntity.builder().firstName("Andrea").familyName("Álvarez").email("aamarinho@gmail.com")
                        .password("22222").matchingPassword("22222").gender(Gender.FEMALE).roles(roles.subList(1, 2)).enabled(true).birthDate(LocalDateTime.now()).build()
        };
        for (UserEntity userEntitie : userEntities) {
            userEntitie.setPassword(passwordEncoder.encode(userEntitie.getPassword()));
        }
        LogManager.getLogger(this.getClass()).warn(this.userDao.saveAll(List.of(userEntities)));
        LogManager.getLogger(this.getClass()).warn("------- Finish Load from JAVA -----------");

    }
}
