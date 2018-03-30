package pl.paweljvm.sbv.runner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import pl.paweljvm.sbv.config.AppConfig;

@SpringBootApplication
@Import(AppConfig.class)
public class Runner {

    public static void main(String...args) {
        SpringApplication.run(Runner.class, args);
    }
}
