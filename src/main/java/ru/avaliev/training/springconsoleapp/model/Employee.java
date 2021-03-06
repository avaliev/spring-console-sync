package ru.avaliev.training.springconsoleapp.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Table("employees")
@Data
public class Employee {

    @Id
    private Long id;

    private String depCode;

    private String depJob;

    private String description;

}
