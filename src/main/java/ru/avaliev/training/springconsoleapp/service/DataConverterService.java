package ru.avaliev.training.springconsoleapp.service;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Service;
import ru.avaliev.training.springconsoleapp.model.Employee;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * отвечает за конвертацию из xml/json в объекты модели и обратно
 * скрывает библиотеку и его настройки конвертации
 */
@Service
public class DataConverterService {

    private final ObjectMapper jsonMapper;

    private final XmlMapper xmlMapper;

    public DataConverterService() {
        //init and config json converter
        jsonMapper = new ObjectMapper();
        jsonMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // init and config xml converter
        xmlMapper = new XmlMapper();
        xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }


    public <T> List<T> readXmlDataFrom(String content, Class<T> clazz) throws JsonProcessingException {
        List<T> list = xmlMapper.readValue(content, constructListOfType(clazz));
        return list;
    }

    public <T> List<T> readJsonDataFrom(String content, Class<T> clazz) throws JsonProcessingException {
        List<T> list = jsonMapper.readValue(content, constructListOfType(clazz));
        return list;
    }


    public void convertToXml(List<Employee> employees, Writer writer) throws IOException {
        xmlMapper.writer().writeValue(writer, employees);
    }

    public void convertToJson(List<Employee> employee, Writer writer) throws IOException {
        jsonMapper.writeValue(writer, employee);
    }

    private JavaType constructListOfType(Class<?> clazz) {
        return jsonMapper.getTypeFactory().constructCollectionType(List.class, clazz);
    }


}
