package miw_padel_back.infraestructure.api.resources;

import miw_padel_back.domain.model.User;
import miw_padel_back.domain.services.UserService;
import miw_padel_back.infraestructure.mongodb.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping(UserResource.USER)
public class UserResource {
    public static final String USER = "/user";
    private final UserService userService;

    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/create")
    public String create(){
        this.userService.create(User.builder().firstName("Diego").familyName("Lusqui").email("aa")
                .password("123123").build());
        return "INTENTO";
    }
}
