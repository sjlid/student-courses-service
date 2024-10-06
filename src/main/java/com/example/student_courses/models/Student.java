package com.example.student_courses.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Table(name = "students", indexes = @Index(columnList = "student_card"))
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Student's name must be filled")
    private String name;

    @NotBlank(message = "Student's card number must be filled")
    private Long studentCard;
}
