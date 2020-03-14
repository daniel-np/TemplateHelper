package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.ConfigModel;

import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class SettingsScene {
    private Stage parentStage;
    private Scene parentScene;
    private Scene settingsScene;
    private ConfigModel configModel;
    private Map<String, String> configData;
    private TextArea settingsOutputTextArea;

    SettingsScene(Stage parent, ConfigModel configModel) {
        this.configModel = configModel;
        this.parentStage = parent;
        this.parentScene = parent.getScene();

        HBox hBox = new HBox(addSettingFields(),settingsTextArea());
        VBox vBox = new VBox(hBox, submitButtonBar());
        settingsScene = new Scene(vBox);
    }

    private Node submitButtonBar() {
        Button doneButton = new Button("Done");
        doneButton.setOnAction(e->{
            saveChanges();
            this.parentStage.setScene(parentScene);
            this.parentStage.show();
        });
        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e->{
            this.parentStage.setScene(parentScene);
            this.parentStage.show();
        });

        HBox hBox = new HBox(doneButton, cancelButton);
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(5,5,5,5));
        hBox.setAlignment(Pos.BOTTOM_RIGHT);

        return hBox;
    }

    private void saveChanges() {
        configModel.setPermanentFields(configData);
        configModel.writePermanentFieldValuesToConfigFile();
    }

    private Node settingsTextArea() {
        settingsOutputTextArea = new TextArea();
        settingsOutputTextArea.setDisable(true);
        settingsOutputTextArea.setMaxSize(400, 400);
        settingsOutputTextArea.clear();
        this.configModel.getPermanentFields().forEach((k,v)-> settingsOutputTextArea.appendText(k+"="+v+"\n"));
        this.configData = this.configModel.getPermanentFields();

        return settingsOutputTextArea;
    }

    private Node addSettingFields() {
        // Property/key textField
        TextField settingKeyTF = new TextField();
        settingKeyTF.setPromptText("Property");
        Label settingKeyLabel = new Label("Property");
        settingKeyLabel.setLabelFor(settingKeyTF);

        // Value textField
        TextField settingValueTF = new TextField();
        settingValueTF.setPromptText("Value");
        Label settingValueLabel = new Label("Value");
        settingValueLabel.setLabelFor(settingValueTF);

        // Buttons
        Button addButton = new Button("Add");
        addButton.setOnAction(e-> addSettingFromOutputArea(settingKeyTF.getText(), settingValueTF.getText()));
        Button removeButton = new Button("Remove");
        removeButton.setOnAction(e-> removeSettingsFromOutputArea(settingKeyTF.getText()));
        Button clearButton = new Button("Clear");
        clearButton.setOnAction(e->{
            settingKeyTF.clear();
            settingValueTF.clear();
        });

        HBox hBox = new HBox(addButton, removeButton, clearButton);
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(5,5,5,5));
        VBox vBox = new VBox(settingKeyLabel, settingKeyTF, settingValueLabel, settingValueTF, hBox);
        vBox.setPadding(new Insets(5,5,5,5));
        return vBox;
    }

    private void removeSettingsFromOutputArea(String property) {
        Scanner scan = new Scanner(settingsOutputTextArea.getText());
        String line;

        Pattern p = Pattern.compile("^<{2}\\$(" + property + ")(.)+$", Pattern.MULTILINE);
        Matcher m;
        StringBuilder newText = new StringBuilder();
        while(scan.hasNextLine()) {
            line = scan.nextLine();
            m = p.matcher(line);
            if (m.find()) {
                newText.append(line);
                this.configData.remove("<<$" + property + ">>");
                settingsOutputTextArea.clear();
                this.configData.forEach((k,v)-> settingsOutputTextArea.appendText(k+"="+v+"\n"));
            }
        }
    }

    private void addSettingFromOutputArea(String property, String value) {
        this.configData.put("<<$" + property + ">>", value);
        settingsOutputTextArea.clear();
        this.configData.forEach((k,v)-> settingsOutputTextArea.appendText(k+"="+v+"\n"));
    }


    Scene getSettingsScene() {
        return settingsScene;
    }
}
