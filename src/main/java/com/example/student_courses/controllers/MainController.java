package com.example.student_courses.controllers;

import com.example.student_courses.dtos.CourseDto;
import com.example.student_courses.models.Course;
import com.example.student_courses.services.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class MainController {

    private final CourseService courseService;

    public MainController(CourseService courseService) {
        this.courseService = courseService;
    }

    @Operation(summary = "Get all available courses", description = "Get all available for registration courses (courses with registration in future too")
    @GetMapping("/v1/courses")
    public List<CourseDto> getAllAvailableCourses() {
        return courseService.getAvailableCourses()
                .stream()
                .map(this::convertToCourseDto)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Registration to course", description = "Students can register on courses here")
    @PostMapping("/v1/registration")
    public ResponseEntity<HttpStatus> registerOnCourse(
            @Parameter(description = "Number of student card", required = true)
            @RequestParam Long studentCard,
            @Parameter(description = "Course ID. It's the same as in DB", required = true)
            @RequestParam Long courseId
    ) {
        courseService.registerOnCourse(studentCard, courseId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private CourseDto convertToCourseDto(Course course) {
        CourseDto courseDto = new CourseDto();
        courseDto.setCourseName(course.getCourseName());
        courseDto.setTotalSeats(course.getTotalSeats());
        courseDto.setBookedSeats(course.getBookedSeats());
        courseDto.setStartTime(course.getStartTime());
        courseDto.setEndTime(course.getEndTime());
        return courseDto;
    }
}
