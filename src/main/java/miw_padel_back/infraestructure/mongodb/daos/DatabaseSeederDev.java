package miw_padel_back.infraestructure.mongodb.daos;

import miw_padel_back.configuration.security.PBKDF2Encoder;
import miw_padel_back.domain.models.CoupleState;
import miw_padel_back.domain.models.Gender;
import miw_padel_back.domain.models.PaddleCourtType;
import miw_padel_back.domain.models.Role;
import miw_padel_back.infraestructure.mongodb.daos.synchronous.*;
import miw_padel_back.infraestructure.mongodb.entities.*;
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
    public static final String ADM_PASSWORD = "11111";
    public static final String PLAYER_PASSWORD = "22222";
    public static final String COUPLE_PASSWORD = "33333";
    public static final int MAX_COUPLES = 12;
    public static final int ONE_DAY_TO_ADD = 1;
    public static final int TEN_DAYS_TO_ADD = 10;
    private final UserDao userDao;
    private final PaddleCourtDao paddleCourtDao;
    private final BookingDao bookingDao;
    private final CoupleDao coupleDao;
    private final LeagueDao leagueDao;
    private final ImageDao imageDao;
    private final PBKDF2Encoder passwordEncoder;

    @Autowired
    public DatabaseSeederDev(UserDao userDao, PaddleCourtDao paddleCourtDao, BookingDao bookingDao, CoupleDao coupleDao, LeagueDao leagueDao, ImageDao imageDao, PBKDF2Encoder passwordEncoder) {
        this.userDao = userDao;
        this.paddleCourtDao = paddleCourtDao;
        this.bookingDao = bookingDao;
        this.coupleDao = coupleDao;
        this.leagueDao = leagueDao;
        this.imageDao = imageDao;
        this.passwordEncoder = passwordEncoder;
        this.seedDataBase();
    }

    private void seedDataBase() {
        this.imageDao.deleteAll();
        this.leagueDao.deleteAll();
        this.coupleDao.deleteAll();
        this.paddleCourtDao.deleteAll();
        this.bookingDao.deleteAll();
        this.userDao.deleteAll();
        LogManager.getLogger(this.getClass()).warn("------- Initial Load from JAVA -----------");
        var userEntities = new UserEntity[]{
                UserEntity.builder().firstName("Admin").familyName("Admin").email("admin@admin.com")
                        .password(ADM_PASSWORD).matchingPassword(ADM_PASSWORD).gender(Gender.MALE).roles(Collections.singletonList(Role.ROLE_ADMIN)).enabled(true).birthDate(LocalDate.EPOCH).build(),
                UserEntity.builder().firstName("Player").familyName("Player").email("player@player.com")
                        .password(PLAYER_PASSWORD).matchingPassword(PLAYER_PASSWORD).gender(Gender.FEMALE).roles(Collections.singletonList(Role.ROLE_PLAYER)).enabled(true).birthDate(LocalDate.EPOCH).build(),
                UserEntity.builder().firstName("Captain").familyName("captain").email("captain@player.com")
                        .password(COUPLE_PASSWORD).matchingPassword(COUPLE_PASSWORD).gender(Gender.MALE).roles(Collections.singletonList(Role.ROLE_PLAYER)).enabled(true).birthDate(LocalDate.EPOCH).build(),
                UserEntity.builder().firstName("NotCaptain").familyName("notCaptain").email("notcaptain@player.com")
                        .password(COUPLE_PASSWORD).matchingPassword(COUPLE_PASSWORD).gender(Gender.FEMALE).roles(Collections.singletonList(Role.ROLE_PLAYER)).enabled(true).birthDate(LocalDate.EPOCH).build()
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
                        .timeRange(HOUR_10 + " - " + HOUR_12).build(),
                BookingEntity.builder()
                        .user(userEntities[1])
                        .paddleCourt(paddleCourtEntities[1])
                        .date(LocalDate.EPOCH)
                        .timeRange(HOUR_12 + " - " + HOUR_14).build()

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
        var leagues = new LeagueEntity[]{
                LeagueEntity.builder()
                        .name("Mixed League")
                        .gender(Gender.MIXED)
                        .maxCouples(MAX_COUPLES)
                        .startDate(LocalDate.EPOCH.plusDays(ONE_DAY_TO_ADD))
                        .couple(couples[0])
                        .endDate(LocalDate.EPOCH.plusDays(TEN_DAYS_TO_ADD))
                        .build(),

                LeagueEntity.builder()
                        .name("Men's League")
                        .gender(Gender.MALE)
                        .maxCouples(MAX_COUPLES)
                        .startDate(LocalDate.EPOCH.plusDays(ONE_DAY_TO_ADD))
                        .endDate(LocalDate.EPOCH.plusDays(TEN_DAYS_TO_ADD))
                        .build()
                ,
                LeagueEntity.builder()
                        .name("Women's League")
                        .gender(Gender.FEMALE)
                        .maxCouples(MAX_COUPLES)
                        .startDate(LocalDate.EPOCH.plusDays(ONE_DAY_TO_ADD))
                        .endDate(LocalDate.EPOCH.plusDays(TEN_DAYS_TO_ADD))
                        .build()
        };
        this.leagueDao.saveAll(Arrays.asList(leagues));
        LogManager.getLogger(this.getClass()).warn("------- Finish Load from JAVA -----------");

    }
}
