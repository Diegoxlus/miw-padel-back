package miw_padel_back.domain.rest;

import miw_padel_back.configuration.security.JWTUtil;
import miw_padel_back.configuration.security.PBKDF2Encoder;
import miw_padel_back.domain.model.AuthRequest;
import miw_padel_back.domain.model.TokenDto;
import miw_padel_back.domain.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author ard333
 */
@RestController
public class AuthenticationREST {

    private final UserService userService;

    @Autowired
    public AuthenticationREST(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Mono<TokenDto> login(@RequestBody AuthRequest authRequest) {
        return userService.login(authRequest);
    }

}
