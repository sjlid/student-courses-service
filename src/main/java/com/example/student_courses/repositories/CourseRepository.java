package com.example.student_courses.repositories;

import com.example.student_courses.models.Course;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    default Optional<Course> findByIdForUpdate(Long courseId) {
        return findById(courseId);
    }

}
