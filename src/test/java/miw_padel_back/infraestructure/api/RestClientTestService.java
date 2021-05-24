package miw_padel_back.infraestructure.api;

import miw_padel_back.configuration.security.JWTUtil;
import miw_padel_back.domain.models.Role;
import miw_padel_back.domain.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.web.reactive.server.WebTestClient;
import java.util.Arrays;

@Service
public class RestClientTestService {
    private JWTUtil jwtUtil;
    private String token;

    @Autowired
    public RestClientTestService(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

     public WebTestClient login(String email, WebTestClient webTestClient, Role ... roles) {
        User user = User.builder().email(email).roles(Arrays.asList(roles)).build();
        this.token = this.jwtUtil.generateToken(user);
        return webTestClient
                .mutate()
                .defaultHeader("Authorization", "Bearer " + this.token).build();
    }

}
