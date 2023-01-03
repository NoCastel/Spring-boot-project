package classes.student;

import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StudentConfig {

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository repository) {
        return args -> {
            Student megan = new Student("Megan", "megan@mail.com", LocalDate.of(1992, 12, 5));
            Student patrick = new Student("Patrick", "patrick@mail.com", LocalDate.of(1996, 1, 5));
            Student alphonso = new Student("Alphonso", "alphonso@mail.com", LocalDate.of(1998, 7, 5));
            repository.saveAll(List.of(megan, patrick, alphonso));
        };
    }
}
