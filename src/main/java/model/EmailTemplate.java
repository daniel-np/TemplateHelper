package model;

import java.util.HashMap;

public class EmailTemplate {
    private String name, location, templateText;
    private HashMap<String, TemplateTextField> templateFields;

    public EmailTemplate(String name, String location, String templateText, HashMap<String, TemplateTextField> templateFields) {
        this.name = name;
        this.location = location;
        this.templateFields = templateFields;
        this.templateText = templateText;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public HashMap<String, TemplateTextField> getTemplateFields() {
        return templateFields;
    }

    public void setTemplateFields(HashMap<String, TemplateTextField> templateFields) {
        this.templateFields = templateFields;
    }

    public String getTemplateText() {
        return templateText;
    }

    public void setTemplateText(String templateText) {
        this.templateText = templateText;
    }

    @Override
    public String toString() {

        return "Name: " + name + ", \n" +
                "Path: " + location + ", \n" +
                "TemplateText: " + templateText + ", \n";
    }
}
