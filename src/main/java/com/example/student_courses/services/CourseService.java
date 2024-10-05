package com.example.student_courses.services;

import com.example.student_courses.models.Course;
import com.example.student_courses.models.Registration;
import com.example.student_courses.models.Student;
import com.example.student_courses.repositories.CourseRepository;
import com.example.student_courses.repositories.RegistrationRepository;
import com.example.student_courses.repositories.StudentRepository;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final RegistrationRepository registrationRepository;

    public CourseService(CourseRepository courseRepository, StudentRepository studentRepository, RegistrationRepository registrationRepository) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.registrationRepository = registrationRepository;
    }

    @Transactional(readOnly = true)
    public List<Course> getAvailableCourses() {
        Sort sort = Sort.by("courseName");
        return courseRepository.findAll(sort);
    }

    @Transactional
    public void registerOnCourse(long studentId, long courseId) {

        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course with this id doesn't exist"));
        LocalDateTime currentTime = LocalDateTime.now();

        if (currentTime.isBefore(course.getStartTime()) || currentTime.isAfter(course.getEndTime())) {
            throw new RuntimeException("Registration isn't allowed now");
        }
        if (course.getBookedSeats() >= course.getTotalSeats()) {
            throw new RuntimeException("No available seats on this course");
        }

        Student student = studentRepository.findByStudentId(studentId).orElseThrow(() -> new RuntimeException("Student with this StudentID doesn't exist"));

        //registrationRepository.findByCourseIdAndStudentId(courseId, student.getId()).orElseThrow(() -> new RuntimeException("Student is already register to this course"));
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
