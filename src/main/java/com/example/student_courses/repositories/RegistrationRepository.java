package com.example.student_courses.repositories;

import com.example.student_courses.models.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    Optional<Registration> findByCourseIdAndStudentId(long courseId, long studentId);
}
