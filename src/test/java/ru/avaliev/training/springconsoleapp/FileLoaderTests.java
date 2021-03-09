package ru.avaliev.training.springconsoleapp;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.avaliev.training.springconsoleapp.utils.FileResourcesService;

import java.io.IOException;


@SpringBootTest
@Slf4j
public class FileLoaderTests {

    @Autowired
    FileResourcesService fileResourcesService;

    public static final String VALID_XML_FILE = "employees.xml";
    public static final String VALID_JSON_FILE = "employees.json";


    @Test
    public void loadFromFile() throws IOException {
        String content = fileResourcesService.loadFileToString(VALID_JSON_FILE);
        log.info(content);
    }


}
