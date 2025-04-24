package com.infra.easyinfra.Helpers;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class FileOperations {

    public static void createOrOverwriteFile(String directoryPath, String fileName, String content) {
        if (directoryPath == null || fileName == null) {
            System.err.println("Erro: Caminho do diretório ou nome do arquivo é null.");
            return;
        }
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
}
