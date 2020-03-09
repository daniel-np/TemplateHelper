package services;

import model.EmailTemplate;
import model.TemplateTextField;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Pattern;

public class TemplateService {

    public TemplateService() {

    }

    public EmailTemplate parseEmailTemplateFromPath(String path) {
        File templateFile = new File(path);
        String fileName = templateFile.getName();

        try {
            String emailTemplateText = createEmailTemplateText(templateFile);
            HashMap<String, TemplateTextField> templateTextFieldMap = getTemplateFields(templateFile);
            return createEmailTemplate(path, fileName, emailTemplateText, templateTextFieldMap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public EmailTemplate parseEmailTemplateFile(File file) {
        try {
            String emailTemplateText = createEmailTemplateText(file);
            HashMap<String, TemplateTextField> templateTextFieldMap = getTemplateFields(file);
            return createEmailTemplate(file.getPath(), file.getName(), emailTemplateText, templateTextFieldMap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<File> listTemplateFiles(File folder) {
        ArrayList<File> templateFileList = new ArrayList<>();
        for (final File entryFile : Objects.requireNonNull(folder.listFiles())) {
            Optional<String> optExt = getFileExtensionFromName(entryFile.getName());
            if (optExt.isPresent() && optExt.get().equals("template")) {
                templateFileList.add(entryFile);
            }
        }
        return templateFileList;
    }

    private Optional<String> getFileExtensionFromName(String fileName) {
        return Optional.ofNullable(fileName)
                .filter(f->f.contains("."))
                .map(f->f.substring(fileName.lastIndexOf(".")+1));

    }

    private EmailTemplate createEmailTemplate(String path, String fileName, String emailTemplateText, HashMap<String, TemplateTextField> templateTextFieldMap) {
        return new EmailTemplate(
                fileName,
                path,
                emailTemplateText,
                templateTextFieldMap);
    }

    private HashMap<String, TemplateTextField> getTemplateFields(File templateFile) throws FileNotFoundException {
        Scanner scan = new Scanner(templateFile);
        HashMap<String, TemplateTextField> templateTextFieldMap = new HashMap<>();
        scan.findAll(Pattern.compile("<{2}[a-zæøåA-ZÆØÅ0-9]+>{2}"))
                .forEach(item -> {
                    if(templateTextFieldMap.get(item.group()) == null) {
                        TemplateTextField templateTextField = new TemplateTextField(
                                item.end()-item.start(),
                                item.group());
                        templateTextField.addLocation(item.start());
                        templateTextFieldMap.put(item.group(), templateTextField);
                    } else {
                        templateTextFieldMap.get(item.group()).addLocation(item.start());
                    }
                });
        return templateTextFieldMap;
    }

    private String createEmailTemplateText(File templateFile) throws FileNotFoundException {
        Scanner scan = new Scanner(templateFile);
        StringBuilder emailStringBuilder = new StringBuilder();
        while (scan.hasNext()) {
            emailStringBuilder.append(scan.nextLine());
            if (scan.hasNextLine()) {
                emailStringBuilder.append("\n");
            }
        }
        return emailStringBuilder.toString();
    }
}
