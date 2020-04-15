package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import services.TemplateService;

import java.io.FileNotFoundException;

public class TemplateModel {

    private TemplateService templateService;
    private Template currentTemplate;
    private ObservableList<TemplateFile> templateList;

    public TemplateModel(TemplateService templateService) {
        this.templateService = templateService;
    }

    public ObservableList<TemplateFile> loadTemplateFilesFromDirectory(TemplateFile directory) {
        this.templateList = FXCollections.observableArrayList(templateService.listTemplateFiles(directory));
        return templateList;
    }

    public Template loadTemplateFromFile(TemplateFile file) {
        try {
            this.currentTemplate = templateService.parseTemplateFile(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return this.currentTemplate;
    }

    public ObservableList<TemplateFile> getTemplateList() {
        return this.templateList;
    }

    public Template getCurrentTemplate() {
        return this.currentTemplate;
    }
}
