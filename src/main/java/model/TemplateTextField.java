package model;

import java.util.ArrayList;

public class TemplateTextField {
    private int length;
    private ArrayList<Integer> locations;
    private String templateTextField;

    public TemplateTextField(int length, String templateTextField) {
        this.length = length;
        this.templateTextField = templateTextField;
        this.locations = new ArrayList<>();
    }

    public TemplateTextField() {

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
        return templateTextField.substring(2,templateTextField.length()-2) +
                ": " + sb.toString().substring(0, sb.toString().length()-2);
    }

    public String getCleanName() {
        return templateTextField.substring(2,templateTextField.length()-2);
    }
}
