package miw_padel_back.infraestructure.api.resources;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testController {
    @GetMapping("/welcome")
    public String welcome(){
        return "PRUEBA SPRING V2";
    }
}
