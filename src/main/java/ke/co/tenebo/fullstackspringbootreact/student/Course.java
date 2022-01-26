package ke.co.tenebo.fullstackspringbootreact.student;

import java.util.UUID;

public class Course {
    private final UUID courseId;
    private final String name;
    private final String description;
    private final String teacherName;
    private final String department;

    public Course(UUID courseId, String name, String description, String teacherName, String department) {
        this.courseId = courseId;
        this.name = name;
        this.description = description;
        this.teacherName = teacherName;
        this.department = department;
    }

    public UUID getCourseId() {
        return courseId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getDepartment() {
        return department;
    }
}
