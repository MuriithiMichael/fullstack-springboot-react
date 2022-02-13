package ke.co.tenebo.fullstackspringbootreact.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldCheckIfStudentEmailExists() {
        // Given
        String email = "nacho.trent@gmail.com";
        Student student = new Student(
                "Trent",
                "Nacho",
                email,
                Gender.MALE
        );
        underTest.save(student);

        // When
        boolean expected = underTest.selectExistsEmail(email);

        // Then
        assertThat(expected).isTrue();
    }

    @Test
    void itShouldCheckIfStudentEmailDoesNotExist() {
        // Given
        String email = "nacho.trent@gmail.com";

        // When
        boolean expected = underTest.selectExistsEmail(email);

        // Then
        assertThat(expected).isFalse();
    }
}