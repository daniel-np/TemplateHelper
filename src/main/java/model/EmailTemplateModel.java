package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import services.TemplateService;

import java.io.FileNotFoundException;

public class EmailTemplateModel {

    private TemplateService templateService;
    private EmailTemplate currentTemplate;
    private ObservableList<TemplateFile> templateList;

    public EmailTemplateModel(TemplateService templateService) {
        this.templateService = templateService;
    }

    public ObservableList<TemplateFile> loadTemplateFilesFromDirectory(TemplateFile directory) {
        this.templateList = FXCollections.observableArrayList(templateService.listTemplateFiles(directory));
        return templateList;
    }

    public EmailTemplate loadTemplateFromFile(TemplateFile file) {
        try {
            this.currentTemplate = templateService.parseEmailTemplateFile(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return this.currentTemplate;
    }

    public ObservableList<TemplateFile> getTemplateList() {
        return this.templateList;
    }

    public EmailTemplate getCurrentTemplate() {
        return this.currentTemplate;
    }
}
