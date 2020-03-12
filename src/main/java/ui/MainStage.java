package ui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.EmailTemplate;
import model.EmailTemplateModel;
import model.TemplateFile;
import model.TemplateTextField;
import org.apache.commons.lang3.StringUtils;
import services.TemplateService;
import javafx.scene.input.Clipboard;

public class MainStage extends Application {
    private TemplateService templateService;
    private EmailTemplateModel emailTemplateModel;
    private ChoiceBox<TemplateFile> templateChoiceBox;
    private ObservableList<Node> templateFieldList;
    private TextArea outputTextArea;
    private final Clipboard clipboard = Clipboard.getSystemClipboard();
    private final ClipboardContent clipboardContent = new ClipboardContent();


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
        HBox hBox = new HBox(vBox, outputField());
        return new Scene(hBox);
    }

    private Node outputField() {
        Label outputTextAreaLabel = new Label("Output text area");

        Button copyTextButton = new Button("Copy text");
        copyTextButton.setOnAction(e -> {
            clipboardContent.putString(outputTextArea.getText());
            clipboard.setContent(clipboardContent);
        });
        Button resetTextButton = new Button("Reset text");
        resetTextButton.setOnAction(e -> outputTextArea.setText(emailTemplateModel.getCurrentTemplate().getTemplateText()));

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.TOP_RIGHT);
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.add(outputTextAreaLabel, 0, 0);
        gridPane.add(copyTextButton, 3, 0);
        gridPane.add(resetTextButton, 4, 0);

        outputTextArea = new TextArea();
        outputTextAreaLabel.setLabelFor(outputTextArea);
        outputTextArea.setPrefWidth(450);
        outputTextArea.setPrefHeight(400);
        outputTextArea.setEditable(false);

        return new VBox(gridPane, outputTextArea);
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
        loadTemplateButton.setOnAction(e -> {
            EmailTemplate emailTemplate = emailTemplateModel.loadTemplateFromFile(templateChoiceBox.getValue());
            outputTextArea.setText(emailTemplate.getTemplateText());
            addFieldsToTemplateFieldLayout(emailTemplate);
        });

        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        gridPane.add(templateChoiceBoxLabel, 0, 0);
        gridPane.add(templateChoiceBox, 1, 0);
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
        templateFieldLayout.minWidth(500);

        ScrollPane scrollPane = new ScrollPane(templateFieldLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPrefHeight(510);
        scrollPane.maxHeight(510);
        scrollPane.setPrefWidth(500);
        scrollPane.maxWidth(500);
        return scrollPane;
    }

    private void addFieldsToTemplateFieldLayout(EmailTemplate emailTemplate) {
        templateFieldList.removeAll();
        templateFieldList.clear();

        int[] templateListCounter = {0};
        emailTemplate.getTemplateFields().forEach((k, v) -> {
            GridPane gridPane = new GridPane();
            gridPane.setVgap(5);
            gridPane.setHgap(5);
            gridPane.setMinWidth(240);
            gridPane.setMaxWidth(250);
            gridPane.setPadding(new Insets(5, 5, 5, 5));
            // Cleaning up name
            String[] cleanNameArray = StringUtils.splitByCharacterTypeCamelCase(v.getCleanName());
            for (int i = 0; i < cleanNameArray.length; i++) {
                cleanNameArray[i] = StringUtils.capitalize(cleanNameArray[i]);
            }

            gridPane.add(new Label(String.join(" ", cleanNameArray)), 0, 0);

            if (v.getFieldType().equals(TemplateTextField.FieldType.STANDARD_FIELD)) {
                TextField textField = new TextField();
                textField.setOnAction(e -> templateFieldList.forEach((n) -> {
                    v.setTemplateTextField(textField.getText());
                    outputTextArea.setText(outputTextArea.getText().replaceAll(k, textField.getText()));
                }));
                textField.setPromptText(v.getCleanName());
                gridPane.add(textField, 0, 1);
                templateFieldList.add(gridPane);
            } else if (v.getFieldType().equals(TemplateTextField.FieldType.CHOICE_FIELD)) {

                ObservableList<String> templateFileObservableList = FXCollections.observableList(v.getChoices());
                ChoiceBox<String> choiceBox = new ChoiceBox<>(templateFileObservableList);
                choiceBox.setValue(choiceBox.getItems().get(0));
                gridPane.add(choiceBox, 0, 1);

                choiceBox.getSelectionModel().selectedIndexProperty().addListener(l-> templateFieldList.forEach((n) -> {
                    v.setTemplateTextField(choiceBox.getValue());
                    outputTextArea.setText(outputTextArea.getText().replaceAll(k, choiceBox.getValue()));
                }));

                templateFieldList.add(templateListCounter[0]++, gridPane);
            }

        });
    }
}
