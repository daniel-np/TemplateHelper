package ui;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.EmailTemplate;
import model.EmailTemplateModel;
import model.TemplateFile;
import services.TemplateService;

public class MainStage extends Application {
    private TemplateService templateService;
    private EmailTemplateModel emailTemplateModel;
    private ChoiceBox<TemplateFile> templateChoiceBox;
    private ObservableList<Node> templateFieldList;


    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        templateService = new TemplateService();
        emailTemplateModel = new EmailTemplateModel(templateService);

        stage.setTitle("Template Creator");
        Scene mainScene = mainScene();

        stage.setScene(mainScene);
        stage.setResizable(false);
        stage.show();
    }

    private Scene mainScene() {
        VBox vBox = new VBox(createTemplateChooseGrid(), createTemplateFieldLayout());
        return new Scene(vBox);
    }

    private Node createTemplateChooseGrid() {
        Label templateChoiceBoxLabel = new Label("Choose template");
        String templateDirPath = "/Users/Daniel/Documents/java/Emailgenerator/src/main/resources/templates";
        TemplateFile templateDirFile = new TemplateFile(templateDirPath);
        ObservableList<TemplateFile> templateFileObservableList =
                emailTemplateModel.loadTemplateFilesFromDirectory(templateDirFile);
        templateChoiceBox = new ChoiceBox<>(templateFileObservableList);
        templateChoiceBox.setValue(templateChoiceBox.getItems().get(0));

        Button loadTemplateButton = new Button("Load template");
        loadTemplateButton.setOnAction(e->{
            EmailTemplate emailTemplate = emailTemplateModel.loadTemplateFromFile(templateChoiceBox.getValue());
            addFieldsToTemplateFieldLayout(emailTemplate);
        });

        GridPane gridPane = new GridPane();
        //gridPane.setGridLinesVisible(true);
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(10,10,10,10));
        //HBox hBox = new HBox(templateChoiceBoxLabel,templateChoiceBox);

        gridPane.add(templateChoiceBoxLabel, 0,0);
        gridPane.add(templateChoiceBox, 1,0);
        gridPane.add(loadTemplateButton, 2, 0);
        return gridPane;
    }

    private Node createTemplateFieldLayout() {
        FlowPane templateFieldLayout = new FlowPane();
        templateFieldLayout.orientationProperty().setValue(Orientation.HORIZONTAL);
        templateFieldLayout.setVgap(10);
        templateFieldList = templateFieldLayout.getChildren();
        templateFieldLayout.setAlignment(Pos.TOP_LEFT);
        templateFieldLayout.maxHeight(Double.NEGATIVE_INFINITY);
        templateFieldLayout.maxWidth(Double.POSITIVE_INFINITY);
        templateFieldLayout.minHeight(Double.NEGATIVE_INFINITY);
        templateFieldLayout.minWidth(400);

        ScrollPane scrollPane = new ScrollPane(templateFieldLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPrefHeight(410);
        scrollPane.maxHeight(410);
        scrollPane.setPrefWidth(400);
        scrollPane.maxWidth(400);
        return scrollPane;
    }

    private void addFieldsToTemplateFieldLayout(EmailTemplate emailTemplate) {
        templateFieldList.removeAll();
        templateFieldList.clear();
        emailTemplate.getTemplateFields().forEach((k,v) -> {
            GridPane gridPane = new GridPane();
            gridPane.setVgap(5);
            gridPane.setHgap(5);
            gridPane.setPadding(new Insets(5,5,5,5));
            gridPane.add(new Label(v.getCleanName()), 0,0);

            TextField textField = new TextField();
            textField.setPromptText(v.getCleanName());
            gridPane.add(textField,0,1);
            templateFieldList.add(gridPane);
        });
    }
}
