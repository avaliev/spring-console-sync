package ru.avaliev.training.springconsoleapp.utils;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
public class FileResourcesService {

    public String loadFileToString(String path) throws IOException {
        InputStream inputStream = FileResourcesService.class.getClassLoader().getResourceAsStream(path);
        return readFromInputStream(inputStream);
    }


    public  Writer openNewFile(String filename) throws IOException {
        return Files.newBufferedWriter(Paths.get(".", filename),
                StandardOpenOption.CREATE_NEW);
    }


    private String readFromInputStream(InputStream inputStream) throws IOException {
        InputStreamReader streamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(streamReader);
        StringBuilder stringBuilder = new StringBuilder();
        String buf;
        while ((buf = bufferedReader.readLine()) != null) {
            stringBuilder.append(buf);
        }
        return stringBuilder.toString();
    }
}
