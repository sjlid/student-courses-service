package com.example.student_courses.services;

import com.example.student_courses.models.Course;
import com.example.student_courses.models.Registration;
import com.example.student_courses.models.Student;
import com.example.student_courses.repositories.CourseRepository;
import com.example.student_courses.repositories.RegistrationRepository;
import com.example.student_courses.repositories.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CourseServiceTest {
    @InjectMocks
    private CourseService courseService;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private RegistrationRepository registrationRepository;

    @Mock
    private StudentRepository studentRepository;

    private Course course;
    private Student student;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        course = new Course();
        course.setId(1L);
        course.setCourseName("Test Course");
        course.setTotalSeats(2);
        course.setBookedSeats(0);
        course.setStartTime(ZonedDateTime.now().minusDays(1));
        course.setEndTime(ZonedDateTime.now().plusDays(1));

        student = new Student();
        student.setId(1L);
        student.setStudentCard(123123L);
    }

    @Test
    public void testEnrollStudent_Success() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(studentRepository.findByStudentCard(123123L)).thenReturn(Optional.of(student));
        when(registrationRepository.findByCourseIdAndStudentId(1L, student.getId())).thenReturn(Optional.empty());

        courseService.registerOnCourse(123123L, 1L);

        assertThat(course.getBookedSeats()).isEqualTo(1);
        verify(registrationRepository).save(any(Registration.class));
    }

    @Test
    public void testEnrollStudent_AlreadyEnrolled() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(studentRepository.findByStudentCard(123123L)).thenReturn(Optional.of(student));
        when(registrationRepository.findByCourseIdAndStudentId(1L, student.getId())).thenReturn(Optional.of(new Registration()));

        assertThatThrownBy(() -> courseService.registerOnCourse(123123L, 1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Student is already enrolled in this course");

        assertThat(course.getBookedSeats()).isEqualTo(0);
        verify(registrationRepository, never()).save(any(Registration.class));
    }

    @Test
    public void testEnrollStudent_NoAvailableSeats() {
        course.setBookedSeats(2);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        assertThatThrownBy(() -> courseService.registerOnCourse(123123L, 1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("No available seats on this course");


        assertThat(course.getBookedSeats()).isEqualTo(2);
        verify(registrationRepository, never()).save(any(Registration.class));
    }

    @Test
    public void testEnrollStudent_EnrollmentWindowClosed() {
        course.setStartTime(ZonedDateTime.now().plusDays(1));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        assertThatThrownBy(() -> courseService.registerOnCourse(123123L, 1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Registration isn't allowed now");

        assertThat(course.getBookedSeats()).isEqualTo(0);
        verify(registrationRepository, never()).save(any(Registration.class));
    }


    @Test
    public void testGetAllCourses_FilterClosedEnrollment() {
        Course closedCourse = new Course();
        closedCourse.setId(2L);
        closedCourse.setCourseName("Closed Course");
        closedCourse.setTotalSeats(2);
        closedCourse.setBookedSeats(0);
        closedCourse.setEndTime(ZonedDateTime.now().minusDays(1));

        Course openCourse = new Course();
        openCourse.setId(1L);
        openCourse.setCourseName("Open Course");
        openCourse.setTotalSeats(2);
        openCourse.setBookedSeats(0);
        openCourse.setEndTime(ZonedDateTime.now().plusDays(1));

        when(courseRepository.findAll()).thenReturn(Collections.singletonList(openCourse));
        List<Course> result = courseService.getAvailableCourses();

        assertThat(result).containsExactly(openCourse);
    }


}
