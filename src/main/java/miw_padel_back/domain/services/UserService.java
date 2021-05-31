package miw_padel_back.domain.services;

import miw_padel_back.configuration.security.JWTUtil;
import miw_padel_back.domain.persistence.UserPersistence;
import miw_padel_back.domain.utils.FileUploadUtils;
import miw_padel_back.infraestructure.api.dtos.TokenDto;
import miw_padel_back.infraestructure.api.dtos.UserLoginDto;
import miw_padel_back.infraestructure.api.dtos.UserRegisterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Service
public class UserService {

    private final UserPersistence userPersistence;
    private final JWTUtil jwtUtil;
    private final String PHOTO_FOLDER = "user-photos/";

    @Autowired
    public UserService(UserPersistence userPersistence, JWTUtil jwtUtil) {
        this.userPersistence = userPersistence;
        this.jwtUtil = jwtUtil;
    }

    public Mono<UserRegisterDto> create(UserRegisterDto userRegisterDto, MultipartFile multipartFile) {
        return this.userPersistence.create(userRegisterDto)
                .doOnSuccess(userRegisterDtoFromRepository ->{
                    try {
                        FileUploadUtils.saveFile(PHOTO_FOLDER,userRegisterDto.getEmail(),multipartFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    public Mono<TokenDto> login(UserLoginDto userLoginDto) {
        return this.userPersistence.login(userLoginDto)
                .flatMap(user -> Mono.just(new TokenDto(this.jwtUtil.generateToken(user)))
                );
    }
}
