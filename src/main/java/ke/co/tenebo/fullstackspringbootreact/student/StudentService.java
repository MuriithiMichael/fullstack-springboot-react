package ke.co.tenebo.fullstackspringbootreact.student;

import ke.co.tenebo.fullstackspringbootreact.EmailValidator;
import ke.co.tenebo.fullstackspringbootreact.exception.ApiRequestException;
import ke.co.tenebo.fullstackspringbootreact.exception.BadRequestException;
import ke.co.tenebo.fullstackspringbootreact.exception.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class StudentService {

    private final StudentDataAccessService studentDataAccessService;
    private final EmailValidator emailValidator;
    private final StudentRepository studentRepository;


    public List<Student> getAllStudents(){
       //return studentDataAccessService.selectAllStudents();
        return studentRepository.findAll();
    }

    public void addStudent(Student student) {
        //TODO : Validate Email
        boolean isValid = emailValidator.test(student.getEmail());
        if(!isValid){
            throw new ApiRequestException(student.getEmail() + " is not valid");
        }
        //TODO : Verify that email is not taken
//        if(studentDataAccessService.isEmailTaken(student.getEmail())){
//            throw new ApiRequestException(student.getEmail() + " is taken");
//        }
        Boolean existsEmail = studentRepository.selectExistsEmail(student.getEmail());
       // List<Student> students = studentRepository.findByEmail(student.getEmail());

        if(existsEmail){
            throw new BadRequestException("Email "+ student.getEmail() + " taken");
        }
        studentRepository.save(student);
        //addStudent(null, student);
    }

    void addStudent(UUID studentId, Student student) {
        UUID newStudentId = Optional.ofNullable(studentId).orElse(UUID.randomUUID());
        studentDataAccessService.insertStudent(newStudentId, student);
        student.setStudentId(newStudentId);
        studentRepository.save(student);
    }

    public List<StudentCourse> getStudentCourses(UUID studentId) {
       return studentDataAccessService.getStudentCourses(studentId);
    }

    public void deleteStudent(UUID studentId) {
        if(!studentRepository.existsById(studentId)){
            throw new NotFoundException("Student with id "+ studentId + "does not exist");
        }
        studentRepository.deleteById(studentId);
    }
}

