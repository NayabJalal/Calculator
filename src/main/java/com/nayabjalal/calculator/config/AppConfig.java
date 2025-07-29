package com.nayabjalal.calculator.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AppConfig {
    private static final String CONFIG_FILE = "src/main/resources/calculator-config.json";
    private static AppConfig instance;
    private Map<String, Object> config;

    private AppConfig() {
        loadConfig();
    }

    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    private void loadConfig() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File configFile = new File(CONFIG_FILE);
            if (configFile.exists()) {
                config = mapper.readValue(configFile, Map.class);
            } else {
                config = getDefaultConfig();
                saveConfig();
            }
        } catch (IOException e) {
            config = getDefaultConfig();
        }
    }

    private Map<String, Object> getDefaultConfig() {
        Map<String, Object> defaultConfig = new HashMap<>();

        // Calculator settings
        Map<String, Object> calculator = new HashMap<>();
        calculator.put("maxHistorySize", 50);
        calculator.put("defaultTheme", "Light");
        calculator.put("soundEnabled", true);
        calculator.put("animationsEnabled", true);
        defaultConfig.put("calculator", calculator);

        // Keyboard shortcuts
        Map<String, String> shortcuts = new HashMap<>();
        shortcuts.put("clear", "C");
        shortcuts.put("equals", "Enter");
        shortcuts.put("backspace", "Backspace");
        shortcuts.put("square_root", "R");
        defaultConfig.put("shortcuts", shortcuts);

        // Error messages
        Map<String, String> errors = new HashMap<>();
        errors.put("divisionByZero", "Cannot divide by zero");
        errors.put("invalidInput", "Invalid input");
        errors.put("overflow", "Number too large");
        errors.put("underflow", "Number too small");
        defaultConfig.put("errors", errors);

        return defaultConfig;
    }

    public void saveConfig() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(CONFIG_FILE), config);
        } catch (IOException e) {
            System.err.println("Failed to save config: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String path) {
        String[] keys = path.split("\\.");
        Object current = config;

        for (String key : keys) {
            if (current instanceof Map) {
                current = ((Map<String, Object>) current).get(key);
            } else {
                return null;
            }
        }
        return (T) current;
    }

    public void set(String path, Object value) {
        String[] keys = path.split("\\.");
        Map<String, Object> current = config;

        for (int i = 0; i < keys.length - 1; i++) {
            current = (Map<String, Object>) current.computeIfAbsent(keys[i], k -> new HashMap<>());
        }
        current.put(keys[keys.length - 1], value);
        saveConfig();
    }
}