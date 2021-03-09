package ru.avaliev.training.springconsoleapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import ru.avaliev.training.springconsoleapp.exception.DuplicateRecordException;
import ru.avaliev.training.springconsoleapp.model.Employee;
import ru.avaliev.training.springconsoleapp.repos.EmployeeBatchRepo;
import ru.avaliev.training.springconsoleapp.repos.EmployeeRepository;
import ru.avaliev.training.springconsoleapp.utils.FileResourcesService;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * основной сервис реализующи логику приложения
 * импорт из xml/json в БД
 * export из БД в указанный файл
 */
@Slf4j
@Service
public class MergeService {

    private final EmployeeRepository employeeRepository;

    private final DataConverterService dataConverterService;

    private final TransactionTemplate transactionTemplate;

    private final EmployeeBatchRepo employeeBatchRepo;

    private final FileResourcesService fileResourcesService;

    public static String XMl_FILE = "xml";
    public static String JSON_FILE = "json";

    public MergeService(EmployeeRepository employeeRepository, DataConverterService dataConverterService, TransactionTemplate transactionTemplate, EmployeeBatchRepo employeeBatchRepo, FileResourcesService fileResourcesService) {
        this.employeeRepository = employeeRepository;
        this.dataConverterService = dataConverterService;
        this.transactionTemplate = transactionTemplate;
        this.employeeBatchRepo = employeeBatchRepo;
        this.fileResourcesService = fileResourcesService;
    }

    public void exportDbToFile(String filename, String type) {
        log.info("export DB data to file: {} ", filename);
        try {
            List<Employee> result = new ArrayList<>();
            employeeRepository.findAll().forEach(o -> result.add(o));
            Writer writer = fileResourcesService.openNewFile(filename);

            if (type.equalsIgnoreCase(XMl_FILE)) {
                dataConverterService.convertToXml(result, writer);
            } else if (type.equalsIgnoreCase(JSON_FILE)) {
                dataConverterService.convertToJson(result, writer);
            } else {
                throw new IllegalArgumentException("Unknown type of file");
            }

        } catch (IOException ex) {
            log.error("Import from DB failed:", ex);
        }
    }


    public void importToDB(String filename) {
        log.info("Starting sync {} file", filename);
        try {
            String content = fileResourcesService.loadFileToString(filename);
            int ind = filename.lastIndexOf('.');
            String ext = filename.substring(ind + 1);

            List<Employee> employees = getData(content, ext);

            // кладем в Set - более удобную структур для синхронизации
            Set<Employee> importingSet = new HashSet<>();
            importingSet.addAll(employees);

            if (importingSet.size() < employees.size()) {
                throw new DuplicateRecordException("File contains duplicated elements!");
            }

            transactionTemplate.executeWithoutResult(status -> {
                Set<Employee> existedSet = new HashSet<>();
                employeeRepository.findAll().forEach(el -> existedSet.add(el));

                // цикл для удаления из БД не представленных в файле элементов
                for (Employee elem : existedSet) {
                    // если есть в БД но нет в источнике - удаляется из БД
                    if (!importingSet.contains(elem)) {
                        employeeRepository.deleteById(elem.getId());
                    }
                }
                existedSet.clear();
                // загружаем заново оставшиеся
                employeeRepository.findAll().forEach(el -> existedSet.add(el));

                List<Employee> newItems = new ArrayList<>();
                List<Employee> updateItems = new ArrayList<>();

                for (Employee item : importingSet) {
                    if (existedSet.contains(item)) {
                        updateItems.add(item);
                    } else {
                        newItems.add(item);
                    }
                }
                employeeBatchRepo.batchInsert(newItems);
                employeeBatchRepo.batchUpdate(updateItems);

            });

        } catch (IOException ex) {
            log.error("Sync from file {} failed: {}", filename, ex);
        } catch (DuplicateRecordException ex) {
            log.error("File containes items with duplicated depCode and depJob");
        }
    }

    private List<Employee> getData(String content, String fileExt) throws JsonProcessingException {
        List<Employee> employees;
        if (fileExt.equalsIgnoreCase(XMl_FILE)) {
            employees = dataConverterService.readXmlDataFrom(content, Employee.class);
        } else if (fileExt.equalsIgnoreCase(JSON_FILE)) {
            employees = dataConverterService.readJsonDataFrom(content, Employee.class);
        } else {
            throw new IllegalArgumentException("Unknown type of file");
        }
        return employees;
    }
}
