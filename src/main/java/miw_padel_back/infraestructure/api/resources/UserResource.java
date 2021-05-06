package miw_padel_back.infraestructure.api.resources;

import miw_padel_back.infraestructure.api.dtos.UserLoginDto;
import miw_padel_back.infraestructure.api.dtos.TokenDto;
import miw_padel_back.domain.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static miw_padel_back.infraestructure.api.resources.UserResource.USER;

/**
 * @author ard333
 */
@RestController
@RequestMapping(UserResource.USER)
public class UserResource {
    public static final String USER = "/user";
    public static final String AUTH = "/login";

    private final UserService userService;

    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = AUTH)
    public Mono<TokenDto> login(@RequestBody UserLoginDto userLoginDto) {
        return userService.login(userLoginDto);
    }

}
