package ke.co.tenebo.fullstackspringbootreact.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class StudentDataAccessService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public StudentDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    List<Student> selectAllStudents() {
        String sql = "SELECT student_id, first_name, last_name, email, gender FROM student";
        return jdbcTemplate.query(sql, mapStudentsFromDb());
    }

    int insertStudent(UUID studentId, Student student) {
        String sql = " INSERT INTO student " +
                "(student_id, first_name, last_name, email, gender) " +
                "VALUES (?, ?, ?, ?, ?::gender)";
        return jdbcTemplate.update(
                sql,
                studentId,
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getGender().name().toUpperCase()
        );
    }

    @SuppressWarnings("ConstantConditions")
    boolean isEmailTaken(String email) {
        String sql = "SELECT EXISTS(SELECT 1 FROM student WHERE email=?)";
        return jdbcTemplate.queryForObject(sql, new Object[]{email}, (resultSet, i) -> resultSet.getBoolean(1));
    }

    private RowMapper<Student> mapStudentsFromDb() {
        return (resultSet, i) -> {
            String studentIdStr = resultSet.getString("student_id");
            UUID studentId = UUID.fromString(studentIdStr);

            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String email = resultSet.getString("email");
            String genderStr = resultSet.getString("gender");
            Gender gender = Gender.valueOf(genderStr);
            return new Student(studentId, firstName, lastName, email, gender);
        };
    }

    List<StudentCourse> getStudentCourses(UUID studentId) {
        String sql = "SELECT * " +
                "FROM course " +
                "JOIN student_course USING (course_id) " +
                "WHERE student_id = ? ";

        return jdbcTemplate.query(sql, new Object[]{studentId}, mapStudentCourseRowMapper());
    }

    private RowMapper<StudentCourse> mapStudentCourseRowMapper() {
        return (resultSet, i) ->
                new StudentCourse(
                        new Course(
                                UUID.fromString(resultSet.getString("course_id")),
                                resultSet.getString("name"),
                                resultSet.getString("description"), resultSet.getString("teacher_name"),
                                resultSet.getString("department")),
                        resultSet.getDate("start_date").toLocalDate(),
                        resultSet.getDate("end_date").toLocalDate(),
                        UUID.fromString(resultSet.getString("student_id")),
                        Optional.ofNullable(resultSet.getString("grade"))
                                .map(Integer::parseInt)
                                .orElse(null)
                );
    }
}
