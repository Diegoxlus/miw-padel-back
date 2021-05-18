package miw_padel_back.infraestructure.api.resources;

import miw_padel_back.domain.exceptions.ForbiddenException;
import miw_padel_back.domain.services.BookingService;
import miw_padel_back.infraestructure.api.Rest;
import miw_padel_back.infraestructure.api.dtos.BookingDto;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@Rest
@RequestMapping(BookingResource.BOOKING)
public class BookingResource {
    public static final String BOOKING = "booking";
    public static final String DATE_REF = "/{dateRef}";

    private final BookingService bookingService;

    public BookingResource(BookingService bookingService) {
        this.bookingService = bookingService;
    }
    @GetMapping()
    public Flux<BookingDto> readBookingByDate(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        return ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication)
                .flatMapMany(authentication -> {
                    System.out.println(authentication.getAuthorities().toString());
                    if (date != null && authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                        return this.bookingService.readBookingByDate(date);
                    }
                    if (date != null && authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PLAYER"))) {
                        String email = authentication.getPrincipal().toString();
                        return this.bookingService.readBookingsByEmail(email);
                    } else {
                        return Flux.error(new ForbiddenException("Forbidden"));
                    }
                });
    }
}
