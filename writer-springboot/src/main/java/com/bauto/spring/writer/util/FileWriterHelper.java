package com.bauto.spring.writer.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileWriterHelper {

    public static void appendLine(String filePath, String line) throws IOException {
        Path path = Paths.get(filePath);

        // Ensure parent directory exists
        Files.createDirectories(path.getParent());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile(), true))) {
            writer.write(line);
            writer.newLine();
        }
    }
}
