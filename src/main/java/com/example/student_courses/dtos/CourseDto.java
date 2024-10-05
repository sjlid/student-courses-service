package com.example.student_courses.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Schema(description = "Course DTO")
@Getter
@Setter
public class CourseDto {

    @Schema(description = "Course name")
    @NotBlank(message = "Course's name must be filled")
    @Column(name = "course_name")
    private String courseName;

    @Schema(description = "Total seats on the course")
    @Column(name = "total_seats")
    private int totalSeats;

    @Schema(description = "Booked seats on the course")
    @Column(name = "booked_seats")
    private int bookedSeats;

    @Schema(description = "Start time for course's availability")
    @Column(name = "start_time")
    private ZonedDateTime startTime;

    @Schema(description = "End time for course's availability")
    @Column(name = "end_time")
    private ZonedDateTime endTime;
}
