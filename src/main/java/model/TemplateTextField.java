package model;

public class TemplateTextField {
    private int start, stop;
    private String templateTextField;

    public TemplateTextField(int start, int stop, String templateTextField) {
        this.start = start;
        this.stop = stop;
        this.templateTextField = templateTextField;
    }

    public TemplateTextField() {

    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getStop() {
        return stop;
    }

    public void setStop(int stop) {
        this.stop = stop;
    }

    public String getTemplateTextField() {
        return templateTextField;
    }

    public void setTemplateTextField(String templateTextField) {
        this.templateTextField = templateTextField;
    }

    @Override
    public String toString() {
        return templateTextField.substring(2,templateTextField.length()-2);
    }
}
