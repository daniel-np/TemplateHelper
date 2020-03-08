package services;

import model.EmailTemplate;
import model.TemplateTextField;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class TemplateService {

    public TemplateService() {

    }

    public EmailTemplate parseEmailTemplateFile(String path) {
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
                .forEach(item->{
                    TemplateTextField templateTextField = new TemplateTextField(
                            item.start(),
                            item.end(),
                            item.group());
                    templateTextFieldMap.put(item.group(), templateTextField);
                });
        return templateTextFieldMap;
    }

    private String createEmailTemplateText(File templateFile) throws FileNotFoundException {
        Scanner scan = new Scanner(templateFile);
        StringBuilder emailStringBuilder = new StringBuilder();
        while(scan.hasNext()){
            emailStringBuilder.append(scan.nextLine());
            if(scan.hasNextLine()) {
                emailStringBuilder.append("\n");
            }
        }
        return emailStringBuilder.toString();
    }
}
