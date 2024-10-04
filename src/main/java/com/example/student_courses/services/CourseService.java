package com.example.student_courses.services;

import com.example.student_courses.models.Course;
import com.example.student_courses.repositories.CourseRepository;
import com.example.student_courses.repositories.StudentRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    public CourseService(CourseRepository courseRepository, StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    public List<Course> getAllCourses() {
        Sort sort = Sort.by("courseName");
        return courseRepository.findAll(sort);
    }

    public List<Course> getAvailableCourses() {
        Sort sort = Sort.by("courseName");
        return courseRepository.findAll(sort);
    }
}
