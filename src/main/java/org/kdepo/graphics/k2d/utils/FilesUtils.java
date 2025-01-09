package org.kdepo.graphics.k2d.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FilesUtils {

    public static List<String> readLines(String pathToFile) {
        BufferedReader reader;
        List<String> lines = new ArrayList<>();

        try {
            reader = new BufferedReader(new FileReader(pathToFile));
            String line = reader.readLine();

            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            throw new RuntimeException("Cannot read file: " + pathToFile, e);
        }

        return lines;
    }

}
