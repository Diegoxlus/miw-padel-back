package miw_padel_back.infraestructure.api.resources;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import miw_padel_back.domain.models.Role;
import miw_padel_back.infraestructure.api.dtos.UserLoginDto;
import miw_padel_back.infraestructure.api.dtos.TokenDto;
import miw_padel_back.domain.services.UserService;
import miw_padel_back.infraestructure.api.dtos.UserRegisterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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
    public Mono<TokenDto> login(@RequestBody UserLoginDto userLoginDto) {
        return userService.login(userLoginDto);
    }

    @PostMapping(value = REGISTER)
    public Mono<UserRegisterDto> create(@RequestBody @Valid UserRegisterDto userRegisterDto) {
        return userService.create(userRegisterDto);
    }

    @GetMapping(value = "/prueba")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<TokenDto> test(){
        return Mono.just(new TokenDto("PRUEBA"));
    }

}
