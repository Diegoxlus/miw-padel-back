package miw_padel_back.infraestructure.api.resources;

import miw_padel_back.domain.model.User;
import miw_padel_back.domain.services.UserService;
import miw_padel_back.infraestructure.mongodb.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class TestController {

    private final UserService userService;

    @Autowired
    public TestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/create")
    public String welcome(){
        this.userService.create(User.builder().firstName("Diego").familyName("Lusqui").email("aa")
                .password("123123").build());
        return "INTENTO";
    }
}
