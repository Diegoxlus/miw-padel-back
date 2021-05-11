package miw_padel_back.infraestructure.mongodb.daos;

import miw_padel_back.configuration.security.PBKDF2Encoder;
import miw_padel_back.domain.models.Gender;
import miw_padel_back.domain.models.Role;
import miw_padel_back.infraestructure.mongodb.daos.synchronous.UserDao;
import miw_padel_back.infraestructure.mongodb.entities.UserEntity;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
        this.userDao.deleteAll();
        LogManager.getLogger(this.getClass()).warn("------- Initial Load from JAVA -----------");
        var userEntities = new UserEntity[] {
                UserEntity.builder().firstName("Diego").familyName("Lusqui").email("lusky1996@gmail.com")
                        .password("11111").matchingPassword("11111").gender(Gender.MALE).roles(Collections.singletonList(Role.ROLE_ADMIN)).enabled(true).birthDate(LocalDate.now()).build(),
                UserEntity.builder().firstName("Andrea").familyName("Álvarez").email("aamarinho@gmail.com")
                        .password("22222").matchingPassword("22222").gender(Gender.FEMALE).roles(Collections.singletonList(Role.ROLE_PLAYER)).enabled(true).birthDate(LocalDate.now()).build()
        };
        for (UserEntity userEntity : userEntities) {
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        }
        this.userDao.saveAll(List.of(userEntities));
        LogManager.getLogger(this.getClass()).warn("------- Finish Load from JAVA -----------");

    }
}
