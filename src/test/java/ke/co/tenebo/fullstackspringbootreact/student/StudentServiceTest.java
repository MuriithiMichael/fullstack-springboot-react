package ke.co.tenebo.fullstackspringbootreact.student;

import ke.co.tenebo.fullstackspringbootreact.EmailValidator;
import ke.co.tenebo.fullstackspringbootreact.exception.ApiRequestException;
import ke.co.tenebo.fullstackspringbootreact.exception.BadRequestException;
import ke.co.tenebo.fullstackspringbootreact.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock private StudentRepository studentRepository;
    private StudentDataAccessService studentDataAccessService;
    private EmailValidator emailValidator = new EmailValidator();
    private StudentService underTest;

    @BeforeEach
    void setUp() {
        underTest = new StudentService(studentDataAccessService, emailValidator, studentRepository);
    }

    @Test
    void canGetAllStudents() {
        // Given
        // When
        underTest.getAllStudents();
        // Then
        verify(studentRepository).findAll();
    }

    @Test
    void canAddStudent() {
        // Given
        String email = "nacho.trent@gmail.com";
        Student student = new Student(
                "Trent",
                "Nacho",
                email,
                Gender.MALE
        );

        // When
        underTest.addStudent(student);

        // Then
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(studentArgumentCaptor.capture());

        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent).isEqualTo(student);
    }

    @Test
    void willThrowWhenEmailIsTaken() {
        // Given
        String email = "nacho.trent@gmail.com";
        Student student = new Student(
                "Trent",
                "Nacho",
                email,
                Gender.MALE
        );

        given(studentRepository.selectExistsEmail(anyString()))
                .willReturn(true);

        // When
        // Then
        assertThatThrownBy(() -> underTest.addStudent(student))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email "+ student.getEmail() + " taken");

        verify(studentRepository, never()).save(any());
    }

    @Test
    void willThrowWhenEmailIsInvalid() {
        // Given
        String email = "nacho.trent@gmail";
        Student student = new Student(
                "Trent",
                "Nacho",
                email,
                Gender.MALE
        );

        // When
        // Then
        assertThatThrownBy(() -> underTest.addStudent(student))
                .isInstanceOf(ApiRequestException.class)
                .hasMessageContaining(student.getEmail() + " is not valid");

        verify(studentRepository, never()).selectExistsEmail(any());
    }

    @Test
    @Disabled
    void itShouldTestAddStudent() {
        // Given
        // When
        // Then
    }

    @Test
    @Disabled
    void itShouldGetStudentCourses() {
        // Given
        // When
        // Then
    }

    @Test
    void willThrowWhenStudentIdDoesNotExist() {
        // Given
        UUID studentUUID = UUID.randomUUID();
        given(studentRepository.existsById(studentUUID))
                .willReturn(false);

        // When
        // Then
        assertThatThrownBy(() -> underTest.deleteStudent(studentUUID))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Student with id "+ studentUUID + "does not exist");

        verify(studentRepository, never()).deleteById(any());
    }

    @Test
    void canDeleteStudent() {
        // Given
        UUID studentUUID = UUID.randomUUID();
        given(studentRepository.existsById(studentUUID))
                .willReturn(true);

        // When
        underTest.deleteStudent(studentUUID);

        // Then
        ArgumentCaptor<UUID> studentUUIDArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(studentRepository).deleteById(studentUUIDArgumentCaptor.capture());

        UUID capturedStudentUUID = studentUUIDArgumentCaptor.getValue();
        assertThat(capturedStudentUUID).isEqualTo(studentUUID);
    }
}