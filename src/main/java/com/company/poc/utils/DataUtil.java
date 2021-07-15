package com.company.poc.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@Slf4j
public class DataUtil {

    private DataUtil() {
    }

    public static Map<String, Object> parseYaml(String text) {
        var yaml = new Yaml();
        InputStream inputStream = new ByteArrayInputStream(text.getBytes());
        return yaml.load(inputStream);
    }

    public static String readTextFromFile(String path) throws IOException {
        log.info("Reading text from file: " + path);
        return Files.readString(Paths.get(path));
    }

    public static String readQueryFromFile(String fileName) throws IOException {
        return readTextFromFile(Configuration.SQL_SCRIPTS_PATH + fileName);
    }

    public static String readStringFromFile(String fileName) throws IOException {
        return readTextFromFile(Configuration.TXT_PATH + fileName);
    }

    public static long countFilesLines(String fileName) throws IOException {
        var path = Paths.get(Configuration.SQL_SCRIPTS_PATH + fileName);
        log.info("Counting lines in file - " + fileName);
        return Files.lines(path).count();
    }

    public static void writeTextToFile(String path, String text) throws IOException {
        log.info("Writing text to file - " + path);
        if (!Files.exists(Paths.get(path))) {
            final var file = new File(path);
            file.createNewFile();
        }
    }

    public static void copyTo(String source, String destination) {
        var sourceDir = new File(source);
        if (sourceDir.isDirectory() && sourceDir.list().length == 0) {
            return;
        }
        log.info("Copying from " + source + " to " + destination);
        try {
            FileUtils.copyDirectory(new File(source), new File(destination));
        } catch (IOException e) {
            log.error("Failed to copy", e);
        }
    }
}
