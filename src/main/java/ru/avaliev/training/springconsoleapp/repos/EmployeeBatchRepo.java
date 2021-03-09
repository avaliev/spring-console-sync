package ru.avaliev.training.springconsoleapp.repos;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.avaliev.training.springconsoleapp.model.Employee;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EmployeeBatchRepo {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Transactional
    public int[] batchInsert(List<Employee> employeeList) {
        Map<String, ?>[] params = mapList(employeeList);
        return jdbcTemplate.batchUpdate(
                "insert into employees (dep_code,dep_job,description) values(:depCode,:depJob,:description);",
                params);
    }

    @Transactional
    public int[] batchUpdate(List<Employee> employeeList) {
        Map<String, ?>[] params = mapList(employeeList);
        return jdbcTemplate.batchUpdate(
                "update employees set description=:description where dep_code=:depCode and dep_job=:depJob;",
                params);
    }


    public Map<String, ?>[] mapList(List<Employee> employeeList) {
        Map<String, Object>[] objects = new HashMap[employeeList.size()];
        for (int i = 0; i < objects.length; i++) {
            Employee e = employeeList.get(i);
            objects[i] = new HashMap<>();
            objects[i].put("depCode", e.getDepCode());
            objects[i].put("depJob", e.getDepJob());
            objects[i].put("description", e.getDescription());
        }
        return objects;
    }
}