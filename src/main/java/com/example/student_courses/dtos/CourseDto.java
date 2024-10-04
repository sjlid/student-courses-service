package com.example.student_courses.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CourseDto {

    @NotBlank(message = "Course's name must be filled")
    @Column(name = "course_name")
    private String courseName;

    @Column(name = "total_seats")
    private int totalSeats;

    @Column(name = "booked_seats")
    private int bookedSeats;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;
}
