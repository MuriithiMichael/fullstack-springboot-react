package ke.co.tenebo.fullstackspringbootreact.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {
    @Query("" +
            "SELECT CASE WHEN COUNT(s) > 0 THEN " +
            "TRUE ELSE FALSE END " +
            "FROM Student s WHERE s.email = ?1")
    Boolean selectExistsEmail(String email);

//    @Query("SELECT s FROM Student s WHERE s.email = ?1")
//    List<Student> findByEmail(String Email);

}
