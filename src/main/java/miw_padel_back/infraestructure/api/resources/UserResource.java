package miw_padel_back.infraestructure.api.resources;

import miw_padel_back.domain.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public String create() {
        // this.userService.create()
        return "TODO";
    }

    @GetMapping("/login")
    public String welcome() {
        return "HOLA";
    }
}
