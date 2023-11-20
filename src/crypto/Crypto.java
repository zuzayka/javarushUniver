package crypto;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Crypto {
    private static final String INCORRECT_INPUT = "Некорректный ввод";
    private static final char[] ALPHABET = {'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я', 'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я', '.', ',', '«', '»', '"', '“', '”', '-', '*', '/', '@', '#', '%', '№', '\\', '\'', '\n', '\t', ':', '!', '?', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', ' '};
    private static final int ALPHABET_LENGTH = ALPHABET.length;
    private static Path path;       // путь к исходному файлу

    private static void encrypt(int key) {
        Path filePath = getPathCrypto("encrypted_");
        getText(key, "encrypted_");
        System.out.println("Результат шифрования в файле " + filePath);
    }

    private static void decrypt(int key) {
        key *= -1;
        Path filePath = getPathCrypto("decrypted_");
        getText(key, "decrypted_");
        System.out.println("Результат расшифровывания в файле " + filePath);
    }

    private static void decryptBruteForce() {
        String bruteForce = "bruteForceDecrypted_";
        int key;
        int max = 0;
        int finalKey = 0;
        for (int i = 1; i < ALPHABET_LENGTH; i++) {
            key = -i;
            char[] chars = getText(key, bruteForce).toCharArray();
            int length = chars.length;
            int limit = Math.min(length - 1, 100);
            int test = 0;
            for (int j = 0; j < limit; j++) {
                for (char c2 : ALPHABET) {
                    if (chars[j] == c2) {
                        test++;
                        break;
                    }
                }
            }
            if (test > max) {
                max = test;
                finalKey = key;
            }
        }
        getText(finalKey, bruteForce);
        System.out.println("Результат расшифровывания в файле " + getPathCrypto(bruteForce));
        System.out.println("Ключ шифра " + (-finalKey));
    }

    //      метод определения названия зашифрованного/расшифрованного файла
    private static Path getPathCrypto(String cryptoDirection) {
        String fileName = path.getFileName().toString();
        return Path.of(path.getParent().toString(), cryptoDirection + fileName);
    }

    //      метод общих процедур для шифрования/расшифровывания текста и для получения массива символов обработанного текста
    private static String getText(int key, String cryptoDirection) {
        StringBuilder stringBuilder = new StringBuilder();
        String crypto = null;
        try {
            List<String> list = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (int i = 0; i < list.size(); i++) {
                stringBuilder.append(list.get(i));
                if (i < list.size() - 1) {
                    stringBuilder.append("\n");
                }
            }
            char[] chars = stringBuilder.toString().toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] != '\n') {
                    chars[i] += key;
                }
            }
            crypto = new String(chars);
            try (PrintWriter out = new PrintWriter(getPathCrypto(cryptoDirection).toString(), StandardCharsets.UTF_8)) {
                out.println(crypto);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return crypto;
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        while (true) {
            while (true) {
                System.out.println("""
                        Введите:
                        1 для шифрования
                        2 для дешифрования если ключ  известен
                        3 для дешифрования с помощью подбора ключа""");
                int keyProcedure = scanner.nextInt();
                int encryptKey;
                if ((keyProcedure == 1 || keyProcedure == 2)) {
                    while (true) {
                        System.out.println("Введите ключ - целое число от 1 до " + (ALPHABET_LENGTH - 1));
                        encryptKey = scanner.nextInt();
                        if ((encryptKey > 0) && (encryptKey <= ALPHABET_LENGTH)) {
                            break;
                        }
                        System.out.println(INCORRECT_INPUT);
                    }

                    path = FileService.checkFileExist(scanner);
                    if (keyProcedure == 1) {
                        encrypt(encryptKey);
                    } else {
                        decrypt(encryptKey);
                    }
                    break;
                } else if (keyProcedure == 3) {
                    path = FileService.checkFileExist(scanner);
                    decryptBruteForce();
                    break;
                }
                System.out.println(INCORRECT_INPUT);
            }
            System.out.println("Введите 'exit' для выхода или введите 'go' для продолжения");
            String checkExit = scanner.next();
            if (checkExit.equals("exit")) {
                scanner.close();
                return;
            }
        }
    }
}
