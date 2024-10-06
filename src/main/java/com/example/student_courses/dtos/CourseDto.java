package com.example.student_courses.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
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
    private String courseName;

    @Schema(description = "Total seats on the course")
    private int totalSeats;

    @Schema(description = "Booked seats on the course")
    private int bookedSeats;

    @Schema(description = "Start time for course's availability")
    private ZonedDateTime startTime;

    @Schema(description = "End time for course's availability")
    private ZonedDateTime endTime;
}
