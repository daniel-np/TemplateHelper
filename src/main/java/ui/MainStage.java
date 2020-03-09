package ui;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.EmailTemplateModel;
import model.TemplateFile;
import services.TemplateService;

import java.io.File;

public class MainStage extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {

        stage.setTitle("Template Creator");
        Scene mainScene = mainScene();

        stage.setScene(mainScene);
        stage.setResizable(false);
        stage.show();
    }

    private Scene mainScene() {
        TemplateService templateService = new TemplateService();
        EmailTemplateModel emailTemplateModel = new EmailTemplateModel(templateService);

        Label templateChoiceBoxLabel = new Label("Choose template");
        String templateDirPath = "/Users/Daniel/Documents/java/Emailgenerator/src/main/resources/templates";
        TemplateFile templateDirFile = new TemplateFile(templateDirPath);
        ObservableList<TemplateFile> templateFileObservableList =
                emailTemplateModel.loadTemplateFilesFromDirectory(templateDirFile);
        ChoiceBox<TemplateFile> templateChoiceBox = new ChoiceBox<>(templateFileObservableList);
        HBox hBox = new HBox(templateChoiceBoxLabel,templateChoiceBox);

        return new Scene(hBox);
    }
}
