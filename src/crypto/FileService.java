package crypto;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class FileService {//     Idea предложила вынести код в отдельный метод для исключения повторов. Проверка существования файла
    private static final String INCORRECT_PATH = "Фала по указанному пути не существует";
    private static final String INPUT_PATH = "Введите путь к файлу";
    static Path checkFileExist(Scanner scanner) {
        scanner.nextLine();
        String filePathString;
        while (true) {
            System.out.println(INPUT_PATH);
            filePathString = scanner.nextLine();
            Path path = Path.of(filePathString);
            if (Files.isRegularFile(path)) {
                break;
            }
            System.out.println(INCORRECT_PATH);
        }
        return Path.of(filePathString);
    }


}