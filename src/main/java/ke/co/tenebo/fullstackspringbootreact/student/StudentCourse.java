package ke.co.tenebo.fullstackspringbootreact.student;

import java.time.LocalDate;
import java.util.UUID;

public class StudentCourse {
    private final Course course;
    private final LocalDate start_date;
    private final LocalDate endDate;
    private final UUID studentId;
    private final Integer grade;

    public StudentCourse(Course course, LocalDate start_date, LocalDate endDate, UUID studentId, Integer grade) {
        this.course = course;
        this.start_date = start_date;
        this.endDate = endDate;
        this.studentId = studentId;
        this.grade = grade;
    }

    public Course getCourse() {
        return course;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public UUID getStudentId() {
        return studentId;
    }

    public Integer getGrade() {
        return grade;
    }
}
