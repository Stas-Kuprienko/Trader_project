package com.trader_project.notifications.domain.message;

import com.trader_project.notifications.exception.ConfigurationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import java.io.*;
import java.util.HashMap;
import java.util.stream.Stream;

@Slf4j
@Component
public class TemplateStore {

    private static final String CLASSPATH = "classpath:";
    private static final String DIRECTORY = "templates";

    private final HashMap<String, String> templates;


    @Autowired
    public TemplateStore() {
        templates = init();
    }


    public String getTemplate(String templateName) {
        String value = templates.get(templateName);
        if (value == null) {
            throw new ConfigurationException("Template is not found: " + templateName);
        } else {
            return value;
        }
    }

    public void addTemplate(String fileName) {
        File[] templateArray;
        try {
            templateArray = ResourceUtils.getFile(CLASSPATH + DIRECTORY).listFiles();
            if (templateArray == null) {
                throw new FileNotFoundException("No template files");
            }
        } catch (FileNotFoundException e) {
            throw new ConfigurationException(e);
        }
        Stream.of(templateArray)
                .filter(file -> (!file.isDirectory() && file.getName().equals(fileName)))
                .forEach(file -> {
                    String key = file.getName().split("\\.")[0];
                    templates.put(key, readFile(file));
                });
    }


    private HashMap<String, String> init() {
        HashMap<String, String> map = new HashMap<>();
        File[] templateArray;
        try {
            templateArray = ResourceUtils.getFile(CLASSPATH + DIRECTORY).listFiles();
            if (templateArray == null) {
                throw new FileNotFoundException("No template files");
            }
        } catch (FileNotFoundException e) {
            throw new ConfigurationException(e);
        }
        Stream.of(templateArray)
                .filter(file -> !file.isDirectory())
                .forEach(file -> {
                    String key = file.getName().split("\\.")[0];
                    map.put(key, readFile(file));
                });

        return map;
    }

    private String readFile(File file) {
        try {
            InputStream input = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            try (input; reader) {
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            }
            log.info("Template file ({}) is loaded", file.getName());
            return stringBuilder.toString();
        } catch (IOException e) {
            throw new ConfigurationException(e);
        }
    }
}
