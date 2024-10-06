package com.example.student_courses.services;

import com.example.student_courses.models.Course;
import com.example.student_courses.models.Registration;
import com.example.student_courses.models.Student;
import com.example.student_courses.repositories.CourseRepository;
import com.example.student_courses.repositories.RegistrationRepository;
import com.example.student_courses.repositories.StudentRepository;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final RegistrationRepository registrationRepository;

    @Transactional(readOnly = true)
    public List<Course> getAvailableCourses() {
        return courseRepository.findAll()
                .stream()
                .filter(course -> course.getBookedSeats() < course.getTotalSeats())
                .filter(course -> course.getEndTime().isAfter(ZonedDateTime.now()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void registerOnCourse(long studentCard, long courseId) {
        Course course = courseRepository.findByIdForUpdate(courseId).orElseThrow(() -> new RuntimeException("Course with this id doesn't exist"));
        ZonedDateTime currentTime = ZonedDateTime.now();

        if (currentTime.isBefore(course.getStartTime()) || currentTime.isAfter(course.getEndTime())) {
            throw new RuntimeException("Registration isn't allowed now");
        }
        if (course.getBookedSeats() >= course.getTotalSeats()) {
            throw new RuntimeException("No available seats on this course");
        }

        Student student = studentRepository.findByStudentCard(studentCard).orElseThrow(() -> new RuntimeException("Student with this StudentID doesn't exist"));

        Optional<Registration> existingEnrollment = registrationRepository.findByCourseIdAndStudentId(courseId, student.getId());
        if (existingEnrollment.isPresent()) {
            throw new RuntimeException("Student is already enrolled in this course");
        }

        Registration registration = new Registration();
        registration.setStudent(student);
        registration.setCourse(course);
        registrationRepository.save(registration);
        course.setBookedSeats(course.getBookedSeats() + 1);
        courseRepository.save(course);
    }
}
