package classes.student;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class StudentService {
    public List<Student> getStudents() {
        return List.of(
                new Student("Megan", "megan@mail.com", LocalDate.of(1992, 12, 5), 23),
                new Student("Patrick", "Patrick@mail.com", LocalDate.of(1992, 12, 5), 23),
                new Student("Alphonso", "Alphonso@mail.com", LocalDate.of(1992, 12, 5), 23));
    }
}
