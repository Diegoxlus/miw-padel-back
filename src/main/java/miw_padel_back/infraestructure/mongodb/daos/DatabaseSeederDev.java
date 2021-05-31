package miw_padel_back.infraestructure.mongodb.daos;

import miw_padel_back.configuration.security.PBKDF2Encoder;
import miw_padel_back.domain.models.CoupleState;
import miw_padel_back.domain.models.Gender;
import miw_padel_back.domain.models.PaddleCourtType;
import miw_padel_back.domain.models.Role;
import miw_padel_back.infraestructure.mongodb.daos.synchronous.BookingDao;
import miw_padel_back.infraestructure.mongodb.daos.synchronous.CoupleDao;
import miw_padel_back.infraestructure.mongodb.daos.synchronous.PaddleCourtDao;
import miw_padel_back.infraestructure.mongodb.daos.synchronous.UserDao;
import miw_padel_back.infraestructure.mongodb.entities.BookingEntity;
import miw_padel_back.infraestructure.mongodb.entities.CoupleEntity;
import miw_padel_back.infraestructure.mongodb.entities.PaddleCourtEntity;
import miw_padel_back.infraestructure.mongodb.entities.UserEntity;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class DatabaseSeederDev {
    public static final String HOUR_10 = "10:00";
    public static final String HOUR_12 = "12:00";
    public static final String HOUR_14 = "14:00";
    public static final String HOUR_16 = "16:00";
    private final UserDao userDao;
    private final PaddleCourtDao paddleCourtDao;
    private final BookingDao bookingDao;
    private final CoupleDao coupleDao;
    private final PBKDF2Encoder passwordEncoder;

    @Autowired
    public DatabaseSeederDev(UserDao userDao, PaddleCourtDao paddleCourtDao, BookingDao bookingDao, CoupleDao coupleDao, PBKDF2Encoder passwordEncoder) {
        this.userDao = userDao;
        this.paddleCourtDao = paddleCourtDao;
        this.bookingDao = bookingDao;
        this.coupleDao = coupleDao;
        this.passwordEncoder = passwordEncoder;
        this.seedDataBase();
    }

    private void seedDataBase() {
        this.coupleDao.deleteAll();
        this.paddleCourtDao.deleteAll();
        this.bookingDao.deleteAll();
        this.userDao.deleteAll();
        LogManager.getLogger(this.getClass()).warn("------- Initial Load from JAVA -----------");
        var userEntities = new UserEntity[]{
                UserEntity.builder().firstName("Admin").familyName("Admin").email("admin@admin.com")
                        .password("11111").matchingPassword("11111").gender(Gender.MALE).roles(Collections.singletonList(Role.ROLE_ADMIN)).enabled(true).birthDate(LocalDate.EPOCH).build(),
                UserEntity.builder().firstName("Player").familyName("Player").email("player@player.com")
                        .password("22222").matchingPassword("22222").gender(Gender.FEMALE).roles(Collections.singletonList(Role.ROLE_PLAYER)).enabled(true).birthDate(LocalDate.EPOCH).build(),
                UserEntity.builder().firstName("Captain").familyName("captain").email("captain@player.com")
                        .password("33333").matchingPassword("33333").gender(Gender.MALE).roles(Collections.singletonList(Role.ROLE_PLAYER)).enabled(true).birthDate(LocalDate.EPOCH).build(),
                UserEntity.builder().firstName("NotCaptain").familyName("notCaptain").email("notcaptain@player.com")
                        .password("33333").matchingPassword("33333").gender(Gender.FEMALE).roles(Collections.singletonList(Role.ROLE_PLAYER)).enabled(true).birthDate(LocalDate.EPOCH).build()
        };

        var paddleCourtEntities = new PaddleCourtEntity[]{
                PaddleCourtEntity.builder().name("PC 1").paddleCourtType(PaddleCourtType.INDOOR)
                        .startTimes(Arrays.asList(HOUR_10, HOUR_12))
                        .endTimes(Arrays.asList(HOUR_12, HOUR_14))
                        .disabled(false)
                        .build(),
                PaddleCourtEntity.builder().name("PC 2").paddleCourtType(PaddleCourtType.INDOOR)
                        .startTimes(Arrays.asList(HOUR_10, HOUR_12, HOUR_14))
                        .endTimes(Arrays.asList(HOUR_12, HOUR_14, HOUR_16))
                        .disabled(false)
                        .build(),
                PaddleCourtEntity.builder().name("PC 3").paddleCourtType(PaddleCourtType.OUTDOOR)
                        .startTimes(Arrays.asList(HOUR_10, HOUR_12, HOUR_14))
                        .endTimes(Arrays.asList(HOUR_12, HOUR_14, HOUR_16))
                        .disabled(false)
                        .build(),
                PaddleCourtEntity.builder().name("PC 4").paddleCourtType(PaddleCourtType.INDOOR)
                        .startTimes(Arrays.asList(HOUR_10, HOUR_12, HOUR_14))
                        .endTimes(Arrays.asList(HOUR_12, HOUR_14, HOUR_16))
                        .disabled(true)
                        .build()
        };

        this.paddleCourtDao.saveAll(List.of(paddleCourtEntities));
        for (UserEntity userEntity : userEntities) {
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        }
        this.userDao.saveAll(List.of(userEntities));

        var bookings = new BookingEntity[]{
                BookingEntity.builder().user(userEntities[0])
                        .paddleCourt(paddleCourtEntities[0])
                        .date(LocalDate.EPOCH)
                        .timeRange("10:00 - 12:00").build(),
                BookingEntity.builder()
                        .user(userEntities[1])
                        .paddleCourt(paddleCourtEntities[1])
                        .date(LocalDate.EPOCH)
                        .timeRange("12:00 - 14:00").build()

        };
        this.bookingDao.saveAll(Arrays.asList(bookings));

        var couples = new CoupleEntity[]{
                CoupleEntity.builder()
                        .captain(userEntities[2])
                        .player(userEntities[3])
                        .coupleState(CoupleState.PENDING)
                        .gender(Gender.MIXED)
                        .creationDate(LocalDate.EPOCH)
                        .build()

        };
        this.coupleDao.saveAll(Arrays.asList(couples));
        LogManager.getLogger(this.getClass()).warn("------- Finish Load from JAVA -----------");

    }
}
