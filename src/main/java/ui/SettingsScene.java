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

class SettingsScene {
    private Stage parentStage;
    private Scene parentScene;
    private Scene settingsScene;
    private ConfigModel configModel;

    SettingsScene(Stage parent, ConfigModel configModel) {
        this.configModel = configModel;
        this.parentStage = parent;
        this.parentScene = parent.getScene();

        HBox hBox = new HBox(settingsTextArea());
        VBox vBox = new VBox(hBox, submitButtonBar());
        settingsScene = new Scene(vBox);
    }

    private Node submitButtonBar() {
        Button doneButton = new Button("Done");
        doneButton.setOnAction(e->{
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

    private Node settingsTextArea() {
        TextArea textArea = new TextArea();
        textArea.setDisable(true);
        textArea.setMaxSize(400, 400);
        this.configModel.getPermanentFields().forEach((k,v)-> textArea.setText(k+"="+v+"\n"));

        return textArea;
    }

    private Node addSettingFields() {
        TextField settingKeyTF = new TextField();
        Label settingKeyLabel = new Label("Property name");
        settingKeyLabel.setLabelFor(settingKeyTF);

        TextField settingValueTF = new TextField("Value");
        Label settingValueLabel = new Label("Value");
        settingValueLabel.setLabelFor(settingValueTF);

        return new VBox(settingKeyLabel, settingKeyTF, settingValueLabel, settingValueTF);
    }


    Scene getSettingsScene() {
        return settingsScene;
    }
}
