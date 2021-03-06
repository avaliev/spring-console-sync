package ru.avaliev.training.springconsoleapp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MergeService {

    public void exportDbToFile(String filename, String type) {
        log.info("export DB data to file: {} ", filename);
    }


    public void importToDB(String filename) {
        log.info("Starting sync {} file", filename);
    }
}
