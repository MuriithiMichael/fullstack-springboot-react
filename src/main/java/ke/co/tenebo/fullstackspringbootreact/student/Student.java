package ke.co.tenebo.fullstackspringbootreact.student;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Student {

    @Id
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @GeneratedValue(generator = "UUID")
    @Column(
            name = "student_id",
            nullable = false,
            updatable = false
    )
    private  UUID studentId;

    @NotBlank(message = "first name cannot be blank")
    @Column(nullable = false)
    private  String firstName;
    @NotBlank
    @Column(nullable = false)
    private String lastName;
    @Email
    @Column(nullable = false, unique = true)
    private String email;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    public Student(String firstName, String lastName, String email, Gender gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
    }
}
