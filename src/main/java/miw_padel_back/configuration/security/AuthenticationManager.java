package miw_padel_back.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final JWTUtil jwtUtil;

    @Autowired
    public AuthenticationManager(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Mono<Authentication> authenticate(Authentication authentication) {
        var authToken = authentication.getCredentials().toString();

        try {
            String username = jwtUtil.getUsernameFromToken(authToken);
            if (Boolean.TRUE.equals(jwtUtil.isTokenExpired(authToken))) {
                return Mono.empty();
            }
            List<String> rolesMap = jwtUtil.getAllClaimsFromToken(authToken).get("role", List.class);
            List<GrantedAuthority> authorities = new ArrayList<>();
            for (String rolemap : rolesMap) {
                authorities.add(new SimpleGrantedAuthority(rolemap));
            }
            return Mono.just(new UsernamePasswordAuthenticationToken(username,authentication.getCredentials(), authorities));
        } catch (Exception e) {
            return Mono.empty();
        }
    }
}
