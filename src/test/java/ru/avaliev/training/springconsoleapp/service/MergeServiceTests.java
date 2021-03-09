package ru.avaliev.training.springconsoleapp.service;


import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.transaction.annotation.Transactional;
import ru.avaliev.training.springconsoleapp.model.Employee;
import ru.avaliev.training.springconsoleapp.repos.EmployeeRepository;
import ru.avaliev.training.springconsoleapp.utils.FileResourcesService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * тестирование общей логики,
 * так как есть конвертации/деконвертации для xml файлов,
 * здесь тесты на примере json файлов
 */
@Transactional
@SpringBootTest
public class MergeServiceTests {


    @SpyBean
    FileResourcesService fileResourcesService;

    @SpyBean
    DataConverterService dataConverterService;

    @Autowired
    MergeService mergeService;

    @Autowired
    EmployeeRepository employeeRepository;

    private Employee createEmpl(String dep, String job, String descr) {
        return new Employee(null, dep, job, descr);
    }

    /**
     * тестирование загрузки в пустую СУБД
     */
    @SneakyThrows
    @Test
    public void importJsonFileToEmptyDB() {
        String jsonFile = "employees.json";
        mergeService.importToDB(jsonFile);
        var iterable = employeeRepository.findAll();
        var actualList = StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
        // я знаю что в файле 5 элементов, для упрощения такое допущение, без загрузки другим способом
        Assertions.assertEquals(5, actualList.size());
    }


    /**
     * загружаем 5 стандартных записей
     * загружаем из дополнительного файла где 3 новый других записей
     * должен добавить 3 из нового файла и обновить 1 старый
     */
    @SneakyThrows
    @Test
    public void importExtraValues() {
        // импортируем первоначальные данные
        String jsonFile = "employees.json";
        String jsonFileExtra = "employees-extra.json";
        mergeService.importToDB(jsonFile);
        var iterable = employeeRepository.findAll();
        var actualList = StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());

        var itemForUpdate = actualList.get(0);

        String extraContent = fileResourcesService.loadFileToString(jsonFileExtra);

        List<Employee> extraData = dataConverterService.readJsonDataFrom(extraContent, Employee.class);

        // используем переопредление метода
        Mockito.doReturn(extraData).when(dataConverterService).readJsonDataFrom(extraContent, Employee.class);
        mergeService.importToDB(jsonFileExtra);
        // должен добавить 3 из нового файла и обновить 1 старый

        iterable = employeeRepository.findAll();
        actualList = StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());

        Assertions.assertTrue(actualList.size() == 8);

    }

    /**
     * тестирование логики загрузки в БД, когда в нем не представленный в файле элемент
     */
    @SneakyThrows
    @Test
    public void should_delete_item_when_importNewJsonFile() {
        Employee empl = createEmpl("department123213123", "job12093u12", "");
        empl = employeeRepository.save(empl);
        String jsonFile = "employees.json";
        mergeService.importToDB(jsonFile);
        var oldItem = employeeRepository.findById(empl.getId());
        Assertions.assertTrue(oldItem.isEmpty());
    }
}
