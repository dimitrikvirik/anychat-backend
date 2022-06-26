package git.dimitrikvirik.anychatbackend;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication

public class AnychatBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnychatBackendApplication.class, args);
        try {
            Files.createDirectories(Paths.get("photos"));
        }catch (IOException e){

        }
    }

}
