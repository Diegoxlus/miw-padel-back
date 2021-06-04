package miw_padel_back.infraestructure.api.resources;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import miw_padel_back.domain.exceptions.ForbiddenException;
import miw_padel_back.domain.services.BookingService;
import miw_padel_back.infraestructure.api.dtos.BookingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN') or hasRole('PLAYER')")
@RestController
@RequestMapping(BookingResource.BOOKING)
public class BookingResource {
    public static final String BOOKING = "/booking";
    public static final String DATE_REF = "/{dateRef}";

    private final BookingService bookingService;

    @Autowired
    public BookingResource(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping()
    public Flux<BookingDto> readBookingsByOptionalDate(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication)
                .flatMapMany(authentication -> {
                    if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                        if (date == null) return this.bookingService.readAll();
                        else return this.bookingService.readBookingsByDate(date);
                    }
                    if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PLAYER"))) {
                        var email = authentication.getPrincipal().toString();
                        if (date == null) return this.bookingService.readBookingsByEmail(email);
                        else return this.bookingService.readBookingsByEmailAndDate(email, date);
                    } else {
                        return Flux.error(new ForbiddenException("Forbidden"));
                    }
                });
    }

    @PostMapping()
    public Mono<BookingDto> create(@RequestBody BookingDto bookingDto) {
        return this.bookingService.create(bookingDto);
    }

    @DeleteMapping
    public Mono<Void> delete(@RequestParam String id) {
        return ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication)
                .flatMap(authentication -> {
                    if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                        return this.bookingService.delete(id);
                    }
                    if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PLAYER"))) {
                        return this.bookingService.deleteMyBooking(id, authentication.getPrincipal().toString());
                    } else return Mono.error(new ForbiddenException("Forbidden"));
                });
    }
}
