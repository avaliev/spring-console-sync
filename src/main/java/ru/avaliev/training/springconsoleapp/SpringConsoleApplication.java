package ru.avaliev.training.springconsoleapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@SpringBootApplication
@EnableJdbcRepositories(basePackages = {
        "ru.avaliev.training.springconsoleapp.repos"
})
public class SpringConsoleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringConsoleApplication.class, args);
    }
}
