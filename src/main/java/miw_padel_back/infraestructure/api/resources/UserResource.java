package miw_padel_back.infraestructure.api.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import miw_padel_back.domain.services.UserService;
import miw_padel_back.infraestructure.api.dtos.TokenDto;
import miw_padel_back.infraestructure.api.dtos.UserLoginDto;
import miw_padel_back.infraestructure.api.dtos.UserRegisterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping(UserResource.USER)
public class UserResource {
    public static final String USER = "/user";
    public static final String AUTH = "/login";
    public static final String REGISTER = "/register";


    private final UserService userService;

    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = AUTH)
    public Mono<TokenDto> login(@RequestBody @Valid UserLoginDto userLoginDto) {
        return userService.login(userLoginDto);
    }

    @PostMapping(value = REGISTER)
    public Mono<UserRegisterDto> create(@RequestPart("user") String userString, @RequestPart("image") MultipartFile multipartFile) throws JsonProcessingException {
        UserRegisterDto userRegisterDto = new ObjectMapper().readValue(userString,UserRegisterDto.class);
        return userService.create(userRegisterDto,multipartFile);
    }

}
