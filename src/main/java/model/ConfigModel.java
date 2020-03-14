package model;

import services.TemplateService;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class ConfigModel {
    private Map<String, String> permanentFields;
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
            this.permanentFields = templateService.parseConfigFile(configFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void writePermanentFieldValuesToConfigFile() {
        try {
            if(Objects.nonNull(permanentFields) && Objects.nonNull(configFile)) {
                PrintWriter printWriter = new PrintWriter(new FileOutputStream(configFile, false));
                permanentFields.forEach((k,v)->{
                    String printString = k + "=" + v;
                    printWriter.println(printString);
                });
                printWriter.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> getPermanentFields() {
        return permanentFields;
    }

    public void setPermanentFields(Map<String, String> permanentFields) {
        this.permanentFields = permanentFields;
    }

    public void addPermanentField(String key, String value) {
        if(Objects.isNull(permanentFields)) {
            permanentFields = new HashMap<>();
            permanentFields.put(key, value);
        } else {
            permanentFields.put(key, value);
        }
    }
}
