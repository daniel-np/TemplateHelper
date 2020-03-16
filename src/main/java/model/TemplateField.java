package model;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TemplateField {
    private int length;
    private ArrayList<Integer> locations;
    private String templateTextField;
    private List<String> choices;
    private FieldType fieldType;

    public enum FieldType {
        STANDARD_FIELD,
        CHOICE_FIELD,
        LARGE_FIELD,
        PERM_FIELD,
        OPTION_FIELD;

        @Override
        public String toString() {
            return StringUtils.capitalize(super.toString().toLowerCase());
        }
    }

    public TemplateField(int length, String templateTextField, FieldType fieldType) {
        this.length = length;
        this.templateTextField = templateTextField;
        this.fieldType = fieldType;
        this.locations = new ArrayList<>();
    }

    public TemplateField(int length, String templateTextField, FieldType fieldType, List<String> choices) {
        this.length = length;
        this.templateTextField = templateTextField;
        this.fieldType = fieldType;
        this.locations = new ArrayList<>();
        this.choices = choices;
    }

    public TemplateField() {

    }

    public List<String> getChoices() {
        return choices;
    }

    public void setChoices(List<String> choices) {
        this.choices = choices;
    }


    public FieldType getFieldType() {
        return fieldType;
    }

    public void setFieldType(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public ArrayList<Integer> getLocations(){
        return locations;
    }

    public void addLocation(int loc) {
        locations.add(loc);
    }

    public String getTemplateTextField() {
        return templateTextField;
    }

    public void setTemplateTextField(String templateTextField) {
        if(this.templateTextField.length() != templateTextField.length()) {
            this.length = templateTextField.length();
        }
        this.templateTextField = templateTextField;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i : locations) {
            sb.append(i).append(", ");
        }
        StringBuilder choicesSb = new StringBuilder();
        if (fieldType.equals(FieldType.CHOICE_FIELD)) {
            this.choices.forEach(i -> choicesSb.append(i).append(","));
        }

        return templateTextField.substring(2,templateTextField.length()-2) +
                ": " + sb.toString().substring(0, sb.toString().length()-2) +
                ": " + choicesSb.toString();
    }

    public String getCleanName() {
        return templateTextField.substring(2,templateTextField.length()-2);
    }
}
