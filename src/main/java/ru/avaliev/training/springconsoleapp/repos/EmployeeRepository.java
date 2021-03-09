package ru.avaliev.training.springconsoleapp.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.avaliev.training.springconsoleapp.model.Employee;


@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {

    Employee findByDepCodeAndDepJob(String depCode, String job);
}
