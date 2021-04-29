package miw_padel_back.infraestructure.api.resources;

import miw_padel_back.domain.model.User;
import miw_padel_back.domain.services.UserService;
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

    @GetMapping("/")
    public String welcome(){
        User user = User.builder()
                .address("kejwhflkewhfnkewjfbhkjwefewf")
                .dni("fewfewfewfewf1")
                .email("aa221dfews")
                .mobile("1231123wedf2312322")
                .firstName("Di12weqdsego22")
                .familyName("LUSlk12ewqdsj22")
                .build();
        return Objects.requireNonNull(this.userService.create(user).block()).getDni();
    }
}
