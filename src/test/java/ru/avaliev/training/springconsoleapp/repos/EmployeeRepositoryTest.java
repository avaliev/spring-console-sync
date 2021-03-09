package ru.avaliev.training.springconsoleapp.repos;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.avaliev.training.springconsoleapp.model.Employee;

import java.util.Arrays;

@Transactional
@Slf4j
@SpringBootTest
public class EmployeeRepositoryTest {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    EmployeeBatchRepo employeeBatchRepo;

    @Test
    void shouldSaveAndFind() {
        var empl = new Employee("depABC", "job238", "described");
        employeeRepository.save(empl);

        var saved = employeeRepository.findByDepCodeAndDepJob(empl.getDepCode(), empl.getDepJob());

        Assertions.assertEquals(empl, saved);
    }

    @Test
    void testBathInsert() {
        var empl = new Employee("depABC2", "job132", "described1");
        var empl2 = new Employee("depVKL", "job238", "described2");
        int[] count = employeeBatchRepo.batchInsert(Arrays.asList(empl, empl2));
        log.info("count" + count);
    }

    @Test
    void testBathUpdate() {
        var empl = new Employee("depABC2", "job132", "described1");
        empl = employeeRepository.save(empl);
        empl.setDescription("newDescription");
        int[] count = employeeBatchRepo.batchUpdate(Arrays.asList(empl));
        var updatedItem = employeeRepository.findById(empl.getId()).get();
        Assertions.assertEquals(empl.getDescription(), updatedItem.getDescription());
    }
}
