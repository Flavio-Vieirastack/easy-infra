package com.infra.easyinfra.Helpers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class FileOperations {

    public static void createOrOverwriteFile(String fileName, String content) {
        String basePath = System.getProperty("user.dir");
        Path directory = Paths.get(basePath + "/temp");
        Path filePath = directory.resolve(fileName);
        try {
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }
            Files.writeString(filePath, content);
        } catch (IOException e) {
            System.err.println("Error" + e.getMessage());
        }
    }

    public static String readFile(String fileName) {
        Path path = Paths.get(fileName);
        try {
            return Files.readString(path);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            return null;
        }
    }
}
