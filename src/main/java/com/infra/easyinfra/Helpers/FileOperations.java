package com.infra.easyinfra.Helpers;

import com.infra.easyinfra.Enum.TempKeys;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class FileOperations {

    public static void createOrOverwriteFile(String fileName, String content) {
        Path filePath = getTempPath().resolve(fileName);
        try {
            if (!Files.exists(getTempPath())) {
                Files.createDirectories(getTempPath());
            }
            Files.writeString(filePath, content);
        } catch (IOException e) {
            System.err.println("Error" + e.getMessage());
        }
    }

    public static void createOrOverwriteFile(String directoryPath, String fileName, String content) {
        Path directory = Paths.get(directoryPath);
        Path filePath = directory.resolve(fileName);
        try {
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
                System.out.println("Diretório criado: " + directory.toAbsolutePath());
            }
            Files.writeString(filePath, content);
            System.out.println("Arquivo criado ou sobrescrito: " + filePath.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Erro ao criar ou sobrescrever o arquivo: " + e.getMessage());
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

    public static void removeTempFiles() {
        for (TempKeys tempKeys : TempKeys.values()) {
            Path filePath = getTempPath().resolve(tempKeys.getKey());
            try {
                if (Files.exists(filePath)) {
                    Files.delete(filePath);
                    System.out.println("Arquivo removido com sucesso: " + filePath);
                } else {
                    System.out.println("O arquivo não existe: " + filePath);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static Path getTempPath() {
        String basePath = System.getProperty("user.dir");
        return Paths.get(basePath + "/temp");
    }
}
