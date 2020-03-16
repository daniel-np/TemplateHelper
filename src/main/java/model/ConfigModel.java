package model;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import services.TemplateService;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class ConfigModel {
    private ObservableMap<String, TemplateField> permanentFields;
    private TemplateService templateService;
    private File configFile;

    public ConfigModel(TemplateService templateService) {
        this.templateService = templateService;
        loadPermanentFieldValuesFromConfigFile();
    }

    private void loadPermanentFieldValuesFromConfigFile() {
        try {
            configFile = new File(
                    Objects.requireNonNull(getClass().getClassLoader().getResource("user_settings.cfg")).getFile()
            );
            this.permanentFields = FXCollections.observableMap(templateService.parseConfigFile(configFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void writePermanentFieldValuesToConfigFile() {
        try {
            if (Objects.nonNull(permanentFields) && Objects.nonNull(configFile)) {
                PrintWriter printWriter = new PrintWriter(new FileOutputStream(configFile, false));
                permanentFields.forEach((k, v) -> {
                    String printString = k + "=" + v.getTemplateTextField();
                    printWriter.println(printString);
                });
                printWriter.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, TemplateField> getPermanentFields() {
        return permanentFields;
    }

    public void setPermanentFields(Map<String, TemplateField> permanentFields) {
        if (!this.getPermanentFields().equals(permanentFields)) {
            this.permanentFields.clear();
            this.permanentFields.putAll(permanentFields);
        }
    }

    public void addPermanentFieldListener(MapChangeListener<String, TemplateField> changeListener) {
        this.permanentFields.addListener(changeListener);
    }

    public void addPermanentField(String key, TemplateField value) {
        if (Objects.isNull(permanentFields)) {
            permanentFields = FXCollections.observableMap(new HashMap<>());
            permanentFields.put(key, value);
        } else {
            permanentFields.put(key, value);
        }
    }
}
