package ru.avaliev.training.springconsoleapp.service;


import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.avaliev.training.springconsoleapp.FileLoaderTests;
import ru.avaliev.training.springconsoleapp.model.Employee;
import ru.avaliev.training.springconsoleapp.utils.FileResourcesService;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class DataConverterServiceTests {

    @Autowired
    DataConverterService dataConverterService;

    @Autowired
    FileResourcesService fileResourcesService;

    private Employee createEmpl(String dep, String job, String descr) {
        return new Employee(null, dep, job, descr);
    }

    private List<Employee> createData() {
        List<Employee> res = new ArrayList();
        res.add(createEmpl("A", "job1", "SDfads"));
        res.add(createEmpl("A", "job2", "sdfadsfa"));
        res.add(createEmpl("A", "job3", null));
        return res;
    }


    @SneakyThrows
    @Test
    public void deserializeJsonData() {
        String content = fileResourcesService.loadFileToString(FileLoaderTests.VALID_JSON_FILE);
        List<Employee> employeeList = dataConverterService.readJsonDataFrom(content, Employee.class);
        Assertions.assertEquals(5, employeeList.size());
        Assertions.assertTrue(employeeList.get(0) instanceof Employee);
    }

    @SneakyThrows
    @Test
    public void deserializeXmlData() {
        String content = fileResourcesService.loadFileToString(FileLoaderTests.VALID_XML_FILE);
        List<Employee> employeeList = dataConverterService.readXmlDataFrom(content, Employee.class);
        Assertions.assertEquals(5, employeeList.size());
        Assertions.assertTrue(employeeList.get(0) instanceof Employee);
    }

    @SneakyThrows
    @Test
    public void serializeToJson() {
        Writer writer = null;
        try {
            writer = new StringWriter();
            dataConverterService.convertToJson(createData(), writer);
        } finally {
            if (writer != null) writer.close();
        }
    }

    @SneakyThrows
    @Test
    public void serializeToXml() {
        Writer writer = null;
        List values = createData();
        try {
            writer = new StringWriter();
            dataConverterService.convertToXml(values, writer);
        } finally {
            if (writer != null) writer.close();
        }
        String value = writer.toString();
        List<Employee> employees = dataConverterService.readXmlDataFrom(value, Employee.class);
        Assertions.assertEquals(values.size(), employees.size());
    }
}
