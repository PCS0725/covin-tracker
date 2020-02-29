package life.springlearning.covintracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CovinTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CovinTrackerApplication.class, args);
    }

}
