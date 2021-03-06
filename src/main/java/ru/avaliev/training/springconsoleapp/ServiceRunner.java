package ru.avaliev.training.springconsoleapp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.avaliev.training.springconsoleapp.service.MergeService;

@Component
@Slf4j
public class ServiceRunner implements CommandLineRunner {

    public static final String SYNC = "sync";
    public static final String EXPORT = "export";

    private final MergeService mergeService;

    @Autowired
    public ServiceRunner(MergeService mergeService) {
        this.mergeService = mergeService;
    }

    /**
     * точка входа для сервиса синхронизации файла и БД
     * принимает варианты команд:
     * sync <filename.[json|xml]> - загрузка из файла в БД
     * export [filename] json|xml выгружает в файл указанного типа
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        if (args.length == 0) {
            log.error("There is no command!");
            return;
        } else if (args[0].equalsIgnoreCase(SYNC)) {
            if (args.length == 1) {
                log.error(SYNC + "command required file: json or xml in current directory");
            } else {
                mergeService.importToDB(args[1]);
            }
        } else if (args[0].equalsIgnoreCase(EXPORT)) {
            if (args.length == 1) {
                log.error(EXPORT + "command required filename as 2d param," +
                        "'json' or 'xml' and type as 3d param");
            }
            if (args.length < 3 || !(args[2].equals("json") || args[2].equals("xml"))) {
                log.error("please specify type json|xml as 3d parameter for 'export' command");
            } else {
                mergeService.exportDbToFile(args[1], args[2]);
            }
        } else {
            log.error("specify sync|export command");
        }
    }
}
