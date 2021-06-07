package miw_padel_back.infraestructure.api.resources;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import miw_padel_back.domain.services.UserService;
import miw_padel_back.infraestructure.api.dtos.TokenDto;
import miw_padel_back.infraestructure.api.dtos.UserLoginDto;
import miw_padel_back.infraestructure.api.dtos.UserRegisterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(UserResource.USER)
public class UserResource {
    public static final String USER = "/user";
    public static final String AUTH = "/login";
    public static final String REGISTER = "/register";
    public static final String PHOTO = "/photo";

    private final UserService userService;

    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = AUTH)
    public Mono<TokenDto> login(@RequestBody @Valid UserLoginDto userLoginDto) {
        return userService.login(userLoginDto);
    }

    @PostMapping(value = REGISTER)
    public Mono<UserRegisterDto> create(@RequestBody @Valid UserRegisterDto userRegisterDto) {
        return userService.create(userRegisterDto);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('PLAYER') or hasRole('ADMIN')")
    @PostMapping(value = PHOTO, consumes = {"multipart/form-data"})
    public Mono<Void> create(@RequestPart(value = "file") FilePart file) {

        return ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication)
                .flatMap(authentication -> file.content().flatMap(dataBuffer -> Flux.just(dataBuffer.asByteBuffer().array()))
                        .collectList().flatMap(listByteArray -> {
                            var byteStream = new ByteArrayOutputStream();
                            listByteArray.forEach(bytes -> {
                                try {
                                    byteStream.write(bytes);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                            return this.userService.saveImage(authentication.getPrincipal().toString(),byteStream.toByteArray());
                        }));
    }

    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('PLAYER') or hasRole('ADMIN')")
    @GetMapping(value = PHOTO, produces = MediaType.IMAGE_PNG_VALUE)
    public Mono<byte[]> getImageAsByteArray(@RequestParam String email){
        return this.userService.loadImage(email);
    }
}
