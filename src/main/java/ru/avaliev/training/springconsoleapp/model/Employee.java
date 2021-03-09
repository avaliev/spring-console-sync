package ru.avaliev.training.springconsoleapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

@Getter
@Setter
@Table("employees")
public class Employee {

    @JsonIgnore
    @Id
    private Long id;

    @NonNull
    private String depCode;

    @NonNull
    private String depJob;

    private String description;

    public Employee() {
    }

    public Employee(Long id, String depCode, String depJob, String description) {
        this.id = id;
        this.depCode = depCode;
        this.depJob = depJob;
        this.description = description;
    }

    public Employee(@NonNull String depCode, @NonNull String depJob, String description) {
        this.depCode = depCode;
        this.depJob = depJob;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return depCode.equals(employee.depCode) &&
                depJob.equals(employee.depJob);
    }

    @Override
    public int hashCode() {
        return Objects.hash(depCode, depJob);
    }
}
