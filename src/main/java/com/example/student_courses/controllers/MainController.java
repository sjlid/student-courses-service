package com.example.student_courses.controllers;

import com.example.student_courses.dtos.CourseDto;
import com.example.student_courses.models.Course;
import com.example.student_courses.services.CourseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class MainController {

    private final CourseService courseService;

    public MainController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/v1/courses")
    public List<CourseDto> getAllCourses() {
        return courseService.getAllCourses()
                .stream()
                .map(this::convertToCourseDto)
                .collect(Collectors.toList());
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
