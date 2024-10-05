package com.example.student_courses.controllers;

import com.example.student_courses.dtos.CourseDto;
import com.example.student_courses.models.Course;
import com.example.student_courses.models.Student;
import com.example.student_courses.services.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpEntity;
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

    @Operation(summary = "Get all courses", description = "Get all available for registration courses")
    @GetMapping("/v1/courses")
    public List<CourseDto> getAllAvailableCourses() {
        return courseService.getAvailableCourses()
                .stream()
                .filter(c -> c.getBookedSeats() < c.getTotalSeats())
                .map(this::convertToCourseDto)
                .collect(Collectors.toList());
    }

    @PostMapping("v1/courses")
    public ResponseEntity<HttpStatus> registerOnCourse(
            @RequestParam Long studentId,
            @RequestParam Long courseId
    ) {
        courseService.registerOnCourse(studentId, courseId);
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
