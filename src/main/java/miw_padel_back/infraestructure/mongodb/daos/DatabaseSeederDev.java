package miw_padel_back.infraestructure.mongodb.daos;

import miw_padel_back.configuration.security.PBKDF2Encoder;
import miw_padel_back.domain.models.Gender;
import miw_padel_back.domain.models.PaddleCourtType;
import miw_padel_back.domain.models.Role;
import miw_padel_back.infraestructure.mongodb.daos.synchronous.BookingDao;
import miw_padel_back.infraestructure.mongodb.daos.synchronous.PaddleCourtDao;
import miw_padel_back.infraestructure.mongodb.daos.synchronous.UserDao;
import miw_padel_back.infraestructure.mongodb.entities.BookingEntity;
import miw_padel_back.infraestructure.mongodb.entities.PaddleCourtEntity;
import miw_padel_back.infraestructure.mongodb.entities.UserEntity;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class DatabaseSeederDev {
    private final UserDao userDao;
    private final PaddleCourtDao paddleCourtDao;
    private final BookingDao bookingDao;
    private final PBKDF2Encoder passwordEncoder;

    @Autowired
    public DatabaseSeederDev(UserDao userDao, PaddleCourtDao paddleCourtDao, BookingDao bookingDao, PBKDF2Encoder passwordEncoder) {
        this.userDao = userDao;
        this.paddleCourtDao = paddleCourtDao;
        this.bookingDao = bookingDao;
        this.passwordEncoder = passwordEncoder;
        this.seedDataBase();
    }

    private void seedDataBase() {
        this.userDao.deleteAll();
        this.paddleCourtDao.deleteAll();
        this.bookingDao.deleteAll();
        LogManager.getLogger(this.getClass()).warn("------- Initial Load from JAVA -----------");
        var userEntities = new UserEntity[] {
                UserEntity.builder().firstName("Diego").familyName("Lusqui").email("lusky1996@gmail.com")
                        .password("11111").matchingPassword("11111").gender(Gender.MALE).roles(Collections.singletonList(Role.ROLE_ADMIN)).enabled(true).birthDate(Date.from(Instant.now())).build(),
                UserEntity.builder().firstName("Andrea").familyName("Álvarez").email("aamarinho@gmail.com")
                        .password("22222").matchingPassword("22222").gender(Gender.FEMALE).roles(Collections.singletonList(Role.ROLE_PLAYER)).enabled(true).birthDate(Date.from(Instant.now())).build()
        };

        var paddleCourtEntities = new PaddleCourtEntity [] {
                PaddleCourtEntity.builder().name("PC 1").paddleCourtType(PaddleCourtType.INDOOR)
                        .startTimes(Arrays.asList("10:00","12:00"))
                        .endTimes(Arrays.asList("12:00","14:00"))
                        .disabled(false)
                        .build(),
                PaddleCourtEntity.builder().name("PC 2").paddleCourtType(PaddleCourtType.INDOOR)
                        .startTimes(Arrays.asList("10:00","12:00","14:00"))
                        .endTimes(Arrays.asList("12:00","14:00","16:00"))
                        .disabled(false)
                        .build()
        };

        this.paddleCourtDao.saveAll(List.of(paddleCourtEntities));
        for (UserEntity userEntity : userEntities) {
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        }
        this.userDao.saveAll(List.of(userEntities));


        var bookings = new BookingEntity[]{
                BookingEntity.builder().user(userEntities[0]).paddleCourt(paddleCourtEntities[0]).date(Date.from(Instant.EPOCH)).timeRange("10:00 - 12:00").build(),
                BookingEntity.builder().user(userEntities[1]).paddleCourt(paddleCourtEntities[1]).date(Date.from(Instant.EPOCH)).timeRange("12:00 - 14:00").build()

        };
        this.bookingDao.saveAll(Arrays.asList(bookings));
        LogManager.getLogger(this.getClass()).warn("------- Finish Load from JAVA -----------");

    }
}
