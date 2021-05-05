package miw_padel_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class}) // Not: /error
public class MiwPadelBackApplication {
    public static void main(String[] args) {
        SpringApplication.run(MiwPadelBackApplication.class, args);
    }

}
