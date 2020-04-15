package model;

import java.util.List;
import java.util.Map;

public class Template {
    private String name, location, templateText;
    private Map<String, TemplateField> templateFields;
    private Map<String, List<String>> choiceDefinitions;

    public Template(String name, String location, String templateText, Map<String, TemplateField> templateFields) {
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

    public Map<String, TemplateField> getTemplateFields() {
        return templateFields;
    }

    public void setTemplateFields(Map<String, TemplateField> templateFields) {
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

    public Map<String, List<String>> getChoiceDefinitions() {
        return choiceDefinitions;
    }

    public void setChoiceDefinitions(Map<String, List<String>> choiceDefinitions) {
        this.choiceDefinitions = choiceDefinitions;
    }
}
