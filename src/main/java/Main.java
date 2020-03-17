import ui.MainStage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private String mainDirPath;
    private String templateDirPath;
    private Map<MainStage.pathMapValues, String> pathMap = new HashMap<>();

    public static void main(String[] args) {
        Main main = new Main();
        if (main.trySetup()) {
            MainStage.startApp(main.pathMap);
        }
    }

    private boolean trySetup() {
        if (createTemplateDir()) {
            return createUserSettingsFile();
        }
        return false;
    }

    private boolean createUserSettingsFile() {
        String userSettingsFilePath = mainDirPath.concat(File.separator).concat("user_settings.cfg");

        File userSettingsFile = new File(userSettingsFilePath);
        boolean isSuccessful = false;
        if (userSettingsFile.exists()) {
            isSuccessful = true;
        } else {
            try {
                if (userSettingsFile.createNewFile()) {
                    System.out.println("user_settings.cfg created successfully!");
                    isSuccessful = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (isSuccessful) {
            pathMap.put(MainStage.pathMapValues.USER_SETTINGS_PATH, userSettingsFile.getPath());
        }
        return isSuccessful;
    }

    private boolean createTemplateDir() {
        String homeDir = System.getProperty("user.home");

        mainDirPath = homeDir.concat(File.separator).concat("TemplateGenerator");

        templateDirPath = mainDirPath.concat(File.separator).concat("Templates");

        File mainDirFile = new File(mainDirPath);
        File templateDirFile = new File(templateDirPath);
        boolean isSuccessful = false;
        if (mainDirFile.exists()) {
            if (templateDirFile.exists()) {
                isSuccessful = true;
            } else {
                isSuccessful = templateDirFile.mkdir();
            }
        } else if (mainDirFile.mkdir()) {
            isSuccessful = templateDirFile.mkdir();
        }

        if (isSuccessful) {
            pathMap.put(MainStage.pathMapValues.MAIN_DIR_PATH, mainDirFile.getPath());
            pathMap.put(MainStage.pathMapValues.TEMPLATE_DIR_PATH, templateDirFile.getPath());
        } else {
            System.out.println("Something went wrong creating or accessing template directory!");
        }

        return isSuccessful;
    }
}
