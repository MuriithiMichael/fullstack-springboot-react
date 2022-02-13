package ke.co.tenebo.fullstackspringbootreact.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import ke.co.tenebo.fullstackspringbootreact.student.Gender;
import ke.co.tenebo.fullstackspringbootreact.student.Student;
import ke.co.tenebo.fullstackspringbootreact.student.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(
        locations = "classpath:application-it.yml"
)
@AutoConfigureMockMvc
public class StudentIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StudentRepository studentRepository;

    private final Faker faker = new Faker();

    @Test
    void canRegisterNewStudent() throws Exception {
        // Given
        String firstName = String.format("%s", faker.name().firstName());
        String lastName = String.format("%s", faker.name().lastName()); 
        String email =  String.format("%s.%s@tenabotreq.com", firstName.trim().toLowerCase(), lastName.trim().toLowerCase());
        String gender = faker.demographic().sex().toUpperCase(Locale.ROOT);
        Student student = new Student(
                firstName,
                lastName,
                email,
                Gender.valueOf(gender)
        );
        // When
        ResultActions resultActions = mockMvc
                .perform(post("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)));
        // Then
        resultActions.andExpect(status().isOk());
        List<Student> students = studentRepository.findAll();
        assertThat(students)
                .usingElementComparatorIgnoringFields("studentId")
                .contains(student);
    }

    @Test
    void canDeleteStudent() throws Exception {
        // Given
        String firstName = String.format("%s", faker.name().firstName());
        String lastName = String.format("%s", faker.name().lastName());
        String email =  String.format("%s.%s@tenabotreq.com", firstName.trim().toLowerCase(), lastName.trim().toLowerCase());
        String gender = faker.demographic().sex().toUpperCase(Locale.ROOT);

        Student student = new Student(
                firstName,
                lastName,
                email,
                Gender.valueOf(gender)
        );

        mockMvc.perform(post("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)));

        MvcResult getStudentsResult = mockMvc.perform(get("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = getStudentsResult.getResponse().getContentAsString();

        List<Student> students = objectMapper.readValue(
                contentAsString,
                new TypeReference<>() {
                }
        );

        UUID studentId = students.stream()
                .filter(s -> s.getEmail().equals(student.getEmail()))
                .map(Student::getStudentId)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("Student with email: %s not found", email)));

        // When
        ResultActions resultActions = mockMvc
                .perform(delete("/api/v1/students/{studentId}", studentId));
        // Then
        resultActions.andExpect(status().isOk());
        boolean exists = studentRepository.existsById(studentId);
        assertThat(exists).isFalse();
    }
}
