package ui;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.*;
import org.apache.commons.lang3.StringUtils;
import services.TemplateService;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

public class MainStage extends Application {
    private TemplateService templateService;
    private TemplateModel templateModel;
    private ChoiceBox<TemplateFile> templateChoiceBox;
    private ObservableList<Node> templateFieldList;
    private TextArea outputTextArea;
    private final Clipboard clipboard = Clipboard.getSystemClipboard();
    private final ClipboardContent clipboardContent = new ClipboardContent();
    private Scene mainScene;
    private Stage mainStage;
    private ConfigModel configModel;
    private static Map<MainStage.pathMapValues, String> pathMap;
    private Button addAllFieldsButton;
    private Button copyTextButton;
    private Button resetTextButton;
    private Button loadTemplateButton;
    private Button settingsButton;

    public enum pathMapValues {
        TEMPLATE_DIR_PATH,
        MAIN_DIR_PATH,
        USER_SETTINGS_PATH
    }

    public static void startApp(Map<MainStage.pathMapValues, String> pathMap) {
        MainStage.pathMap = pathMap;
        launch();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        templateService = new TemplateService();
        templateModel = new TemplateModel(templateService);
        configModel = new ConfigModel(templateService, MainStage.pathMap.get(pathMapValues.USER_SETTINGS_PATH));
        configModel.addPermanentFieldListener(l -> insertPermFields());

    }

    private void insertPermFields() {
        if (Objects.nonNull(outputTextArea) && Objects.nonNull(templateModel.getCurrentTemplate())) {
            configModel.getPermanentFields().forEach((k, v) -> {
                templateModel.getCurrentTemplate().getCurrentTemplateFieldValuesMap().put(k,v.getTemplateTextField());
                outputTextArea.setText(outputTextArea.getText().replaceAll(Pattern.quote(k), v.getTemplateTextField()));
            });

        }
    }

    @Override
    public void start(Stage stage) {
        this.mainStage = stage;

        this.mainStage.setTitle("Template Creator");
        this.mainScene = mainScene();
        this.mainStage.setScene(mainScene);
        createBasicAccelerators();
        this.mainStage.setResizable(false);
        this.mainStage.show();
    }


    private Scene mainScene() {
        VBox vBox = new VBox(createTemplateChooseGrid(), createTemplateFieldLayout());
        HBox hBox = new HBox(vBox, outputField());
        return new Scene(hBox);
    }


    private void createBasicAccelerators() {
        // ctrl+l Load template
        KeyCodeCombination kcLoadTemplate = new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN);
        mainScene.getAccelerators().put(kcLoadTemplate, loadTemplateButton::fire);
        // ctrl+s Settings
        KeyCodeCombination kcSettings = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
        mainScene.getAccelerators().put(kcSettings, settingsButton::fire);
        // ctrl+r Reset text
        KeyCodeCombination kcResetText = new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN);
        mainScene.getAccelerators().put(kcResetText, resetTextButton::fire);
        // ctrl+c Copy text
        KeyCodeCombination kcCopyAllText = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN);
        mainScene.getAccelerators().put(kcCopyAllText, copyTextButton::fire);
        // ctrl+a Add all fields
        KeyCodeCombination kcAddAllFields = new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN);
        mainScene.getAccelerators().put(kcAddAllFields, addAllFieldsButton::fire);
    }

    private void createCustomHotkeys() {
        mainScene.getAccelerators().clear();
        createBasicAccelerators();
        templateModel.getCurrentTemplate().getHotkeyMap().forEach((k,v)->{
            KeyCode keyCode = KeyCode.getKeyCode(k);
            if(keyCode != null) {
            KeyCodeCombination keyCombination = new KeyCodeCombination(keyCode);
                mainScene.getAccelerators().put(keyCombination, ()->{
                    System.out.println(templateModel.getCurrentTemplate().getCurrentTemplateFieldValuesMap().get(v));
                    clipboardContent.putString(templateModel.getCurrentTemplate().getCurrentTemplateFieldValuesMap().get(v));
                    clipboard.setContent(clipboardContent);
                });
            }
        });
    }

    private boolean resetOutputText() {
        if (Objects.nonNull(templateModel) && Objects.nonNull(templateModel.getCurrentTemplate())) {
            outputTextArea.setText(templateModel.getCurrentTemplate().getTemplateText());
            return true;
        }
        return false;
    }

    private Node outputField() {
        Label outputTextAreaLabel = new Label("Output text area");

        this.addAllFieldsButton = new Button("Add all fields");

        addAllFieldsButton.setOnAction(e -> {
            if (resetOutputText()) {
                insertPermFields();
                nodeList.forEach(node -> {
                    if (node instanceof TextField) {
                        if (!((TextField) node).getText().equals("")) {
                            templateModel.getCurrentTemplate().getCurrentTemplateFieldValuesMap().put(node.getId(), ((TextField) node).getText());
                            outputTextArea.setText(outputTextArea.getText().replaceAll(node.getId(), ((TextField) node).getText()));
                        }
                    } else if (node instanceof TextArea) {
                        if (!((TextArea) node).getText().equals("")) {
                            templateModel.getCurrentTemplate().getCurrentTemplateFieldValuesMap().put(node.getId(), ((TextArea) node).getText());
                            outputTextArea.setText(outputTextArea.getText().replaceAll(node.getId(), ((TextArea) node).getText()));
                        }
                    } else if (node instanceof ChoiceBox) {
                        if (!((ChoiceBox) node).getValue().equals("")) {
                            templateModel.getCurrentTemplate().getCurrentTemplateFieldValuesMap().put(node.getId(),((ChoiceBox) node).getValue().toString());
                            outputTextArea.setText(outputTextArea.getText().replaceAll(node.getId(), ((ChoiceBox) node).getValue().toString()));
                        }

                    }
                });
            }
        });

        this.copyTextButton = new Button("Copy text");
        copyTextButton.setOnAction(e -> {
            clipboardContent.putString(outputTextArea.getText());
            clipboard.setContent(clipboardContent);
        });
        this.resetTextButton = new Button("Reset text");
        resetTextButton.setOnAction(e -> resetOutputText());

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.add(outputTextAreaLabel, 0, 0);
        gridPane.add(addAllFieldsButton, 2, 0);
        gridPane.add(copyTextButton, 3, 0);
        gridPane.add(resetTextButton, 4, 0);

        outputTextArea = new TextArea();
        outputTextAreaLabel.setLabelFor(outputTextArea);
        outputTextArea.setPrefWidth(500);
        outputTextArea.setPrefHeight(510);
        outputTextArea.setEditable(false);
        return new VBox(gridPane, outputTextArea);
    }
    static HostServices Host;
    private Node createTemplateChooseGrid() {
        Label templateChoiceBoxLabel = new Label("Choose template");
        String templateDirPath = MainStage.pathMap.get(pathMapValues.TEMPLATE_DIR_PATH);
        TemplateFile templateDirFile = new TemplateFile(templateDirPath);
        ObservableList<TemplateFile> templateFileObservableList =
                templateModel.loadTemplateFilesFromDirectory(templateDirFile);
        templateChoiceBox = new ChoiceBox<>(templateFileObservableList);
        if (templateFileObservableList.isEmpty()) {
            templateChoiceBoxLabel.setText("No templates");
            templateChoiceBox.setDisable(true);
        } else {
            templateChoiceBox.setValue(templateChoiceBox.getItems().get(0));
        }

        Button templateFileLocationButton = new Button("Template directory");
        templateFileLocationButton.setOnAction(e -> {
            /*
                Path partPath = Paths.get(templateDirPath);
                Host = getHostServices();
                Host.showDocument(partPath.toUri().toString());

             */
            templateModel.getCurrentTemplate().getCurrentTemplateFieldValuesMap().forEach((k,v)->{
                System.out.println(k + ":" + v);
            });
        });

        this.loadTemplateButton = new Button("Load template");
        loadTemplateButton.setOnAction(e -> {
            if (Objects.nonNull(templateModel.getCurrentTemplate())) {
                templateModel.getCurrentTemplate().getTemplateFields().clear();
            }
            if (Objects.nonNull(templateChoiceBox.getValue())) {
                Template template = templateModel.loadTemplateFromFile(templateChoiceBox.getValue());
                outputTextArea.setText(template.getTemplateText());
                insertPermFields();
                addFieldsToTemplateFieldLayout(template);
                createCustomHotkeys();
            }
        });

        this.settingsButton = new Button("Settings");
        settingsButton.setOnAction(e -> {
            SettingsScene settingsScene = new SettingsScene(mainStage, configModel);
            mainStage.setScene(settingsScene.getSettingsScene());
            mainStage.show();
        });


        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        gridPane.add(templateFileLocationButton, 0, 0);
        gridPane.add(templateChoiceBox, 1, 0);
        gridPane.add(loadTemplateButton, 2, 0);
        gridPane.add(settingsButton, 3, 0);
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

    private List<Node> nodeList;

    private void addFieldsToTemplateFieldLayout(Template template) {
        templateFieldList.removeAll();
        templateFieldList.clear();
        nodeList = new ArrayList<>();

        int[] templateListCounter = {0};
        template.getTemplateFields().forEach((k, v) -> {
            GridPane gridPane = new GridPane();
            gridPane.setVgap(5);
            gridPane.setHgap(5);
            gridPane.setMinWidth(240);
            gridPane.setMaxWidth(250);
            gridPane.setPadding(new Insets(5, 5, 5, 5));
            // Cleaning up name
            List<String> cleanNameArray = Arrays.asList(StringUtils.splitByCharacterTypeCamelCase(v.getCleanName()));
            int extraSpacing = 0;
            for (int i = 0; i < cleanNameArray.size(); i++) {
                if (cleanNameArray.get(i).equals(" ")) {
                    extraSpacing++;
                } else {
                    String temp = StringUtils.capitalize(cleanNameArray.get(i));
                    cleanNameArray.set(i - extraSpacing, temp);
                }
            }

            String cleanName = String.join(" ", cleanNameArray.subList(0, cleanNameArray.size() - extraSpacing));

            gridPane.add(new Label(cleanName), 0, 0);

            if (v.getFieldType().equals(TemplateField.FieldType.STANDARD_FIELD)) {
                TextField textField = new TextField();
                textField.setId(k);
                textField.setPromptText(cleanName);
                gridPane.add(textField, 0, 1);
                templateFieldList.add(gridPane);
                nodeList.add(textField);
            } else if (v.getFieldType().equals(TemplateField.FieldType.CHOICE_FIELD)) {

                ObservableList<String> templateFileObservableList = FXCollections.observableList(v.getChoices());
                ChoiceBox<String> choiceBox = new ChoiceBox<>(templateFileObservableList);
                choiceBox.setId(k);
                choiceBox.setValue(choiceBox.getItems().get(0));
                gridPane.add(choiceBox, 0, 1);
                templateFieldList.add(templateListCounter[0]++, gridPane);
                nodeList.add(choiceBox);

            } else if (v.getFieldType().equals(TemplateField.FieldType.LARGE_FIELD)) {
                TextArea textArea = new TextArea();
                textArea.setId(k);
                textArea.setPromptText(cleanName);
                textArea.setPrefWidth(450);
                textArea.setPadding(new Insets(0, 0, 0, 5));
                nodeList.add(textArea);
                if (templateListCounter[0] % 2 == 0) {
                    gridPane.setMaxWidth(500);
                    gridPane.add(textArea, 0, 1, 2, 2);
                    templateFieldList.add(templateListCounter[0]++, gridPane);
                    templateFieldList.add(templateListCounter[0]++, new GridPane());
                } else {
                    gridPane.setMaxWidth(500);
                    gridPane.setPrefWidth(500);
                    gridPane.add(textArea, 0, 1, 2, 2);
                    templateFieldList.add(templateListCounter[0]++, new GridPane());
                    templateFieldList.add(templateListCounter[0]++, gridPane);
                    templateFieldList.add(templateListCounter[0]++, new GridPane());
                }
            }

        });
    }
}
