package miw_padel_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;

@SpringBootApplication(exclude = {WebMvcAutoConfiguration.class })
public class MiwPadelBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiwPadelBackApplication.class, args);
	}

}
