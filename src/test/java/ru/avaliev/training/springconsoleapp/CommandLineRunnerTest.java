package ru.avaliev.training.springconsoleapp;


import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled
@SpringBootTest
public class CommandLineRunnerTest {

    @Autowired
    ServiceRunner serviceRunner;

    @Test
    public void should_Not_Fail_When_Illegal_Input() throws Exception {
        String[] args_1 = new String[]{"sync"};
        String[] args_2 = new String[]{"export", "file"};
        String[] args_3 = new String[]{"export", "file", "type"};
        serviceRunner.run(args_1);
        serviceRunner.run(args_2);
        serviceRunner.run(args_3);
    }

    @Test
    public void should_Run_export_command() throws Exception {
        String[] args_1 = new String[]{"export", "file", "json"};
        String[] args_2 = new String[]{"export", "file", "xml"};
        serviceRunner.run(args_1);
        serviceRunner.run(args_2);
    }

}
