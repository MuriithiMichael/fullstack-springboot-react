package ke.co.tenebo.fullstackspringbootreact.student;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/students")
@AllArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public List<Student> getAllStudents(){
//       throw new ApiRequestException("Oops cannot get all students with custom exception");
       return studentService.getAllStudents();
    }

    @PostMapping
    public void addStudent(@RequestBody @Valid Student student){
        studentService.addStudent(student);
    }

    @GetMapping(path = "{studentId}/courses")
    public List<StudentCourse> getStudentCourses(@PathVariable("studentId") UUID studentId){
        return studentService.getStudentCourses(studentId);
    }

    @DeleteMapping(path = "{studentId}")
    public void deleteStudent( @PathVariable("studentId") UUID studentId){
        studentService.deleteStudent(studentId);
    }
}
