package com.nayabjalal.calculator.history;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import com.nayabjalal.calculator.config.AppConfig;

public class CalculationHistory {
    private static final String HISTORY_FILE = "src/main/resources/calculation-history.json";
    private static CalculationHistory instance;
    private List<CalculationEntry> history;
    private final ObjectMapper mapper;

    public static class CalculationEntry {
        private String expression;
        private String result;
        private String timestamp;

        public CalculationEntry() {}

        public CalculationEntry(String expression, String result) {
            this.expression = expression;
            this.result = result;
            this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }

        // Getters and setters
        public String getExpression() { return expression; }
        public void setExpression(String expression) { this.expression = expression; }
        public String getResult() { return result; }
        public void setResult(String result) { this.result = result; }
        public String getTimestamp() { return timestamp; }
        public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

        @Override
        public String toString() {
            return expression + " = " + result + " (" + timestamp.substring(11, 19) + ")";
        }
    }

    private CalculationHistory() {
        this.mapper = new ObjectMapper();
        loadHistory();
    }

    public static CalculationHistory getInstance() {
        if (instance == null) {
            instance = new CalculationHistory();
        }
        return instance;
    }

    private void loadHistory() {
        try {
            File historyFile = new File(HISTORY_FILE);
            if (historyFile.exists()) {
                history = mapper.readValue(historyFile, new  TypeReference<List<CalculationEntry>>() {});
            } else {
                history = new ArrayList<>();
            }
        } catch (IOException e) {
            history = new ArrayList<>();
        }
    }

    public void addCalculation(String expression, String result) {
        history.add(new CalculationEntry(expression, result));

        // Limit history size
        Integer maxSize = AppConfig.getInstance().get("calculator.maxHistorySize");
        if (maxSize != null && history.size() > maxSize) {
            history.remove(0);
        }

        saveHistory();
    }

    private void saveHistory() {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(HISTORY_FILE), history);
        } catch (IOException e) {
            System.err.println("Failed to save history: " + e.getMessage());
        }
    }

    public List<CalculationEntry> getHistory() {
        return new ArrayList<>(history);
    }

    public void clearHistory() {
        history.clear();
        saveHistory();
    }

    public List<CalculationEntry> getRecentHistory(int count) {
        int start = Math.max(0, history.size() - count);
        return new ArrayList<>(history.subList(start, history.size()));
    }
}