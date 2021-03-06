package ewha.efub.zeje;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableJpaAuditing
public class ZejeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZejeApplication.class, args);
	}

}
