package services;

import model.EmailTemplate;
import model.TemplateFile;
import model.TemplateTextField;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Pattern;

public class TemplateService {

    public TemplateService() {

    }

    public EmailTemplate parseEmailTemplateFromPath(String path) {
        TemplateFile templateFile = new TemplateFile(path);
        return parseEmailTemplateFile(templateFile);
    }

    public EmailTemplate parseEmailTemplateFile(TemplateFile file) {
        try {
            String emailTemplateText = createEmailTemplateText(file);
            HashMap<String, TemplateTextField> templateTextFieldMap = parseTemplateFields(emailTemplateText);
            return createEmailTemplate(file.getPath(), file.getName(), emailTemplateText, templateTextFieldMap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<TemplateFile> listTemplateFiles(File folder) {
        ArrayList<TemplateFile> templateFileList = new ArrayList<>();
        for (final File entryFile : Objects.requireNonNull(folder.listFiles())) {
            Optional<String> optExt = getFileExtensionFromName(entryFile.getName());
            if (optExt.isPresent() && optExt.get().equals("template")) {
                templateFileList.add(new TemplateFile(entryFile.getPath()));
            }
        }
        return templateFileList;
    }

    private Optional<String> getFileExtensionFromName(String fileName) {
        return Optional.ofNullable(fileName)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(fileName.lastIndexOf(".") + 1));

    }

    private EmailTemplate createEmailTemplate(String path, String fileName, String emailTemplateText, HashMap<String, TemplateTextField> templateTextFieldMap) {
        return new EmailTemplate(
                fileName,
                path,
                emailTemplateText,
                templateTextFieldMap);
    }

    private HashMap<String, TemplateTextField> parseTemplateFields(String templateText) {
        Scanner scan = new Scanner(templateText);
        HashMap<String, TemplateTextField> templateTextFieldMap = new HashMap<>();
        scan.findAll(Pattern.compile("<{2}[a-zæøåA-ZÆØÅ0-9 ]+>{2}"))
                .forEach(item -> {
                    if (templateTextFieldMap.get(item.group()) == null) {
                        TemplateTextField templateTextField = new TemplateTextField(
                                item.end() - item.start(),
                                item.group(),
                                TemplateTextField.FieldType.STANDARD_FIELD);
                        templateTextField.addLocation(item.start());
                        templateTextFieldMap.put(item.group(), templateTextField);
                    } else {
                        templateTextFieldMap.get(item.group()).addLocation(item.start());
                    }
                });
        return templateTextFieldMap;
    }

    public void checkForPropDefinitions(String templateText) {
        Scanner scan = new Scanner(templateText);
        String nextLine = scan.nextLine();
        List<String> choices;
        if(nextLine.equals("#def")) {
            nextLine = scan.nextLine();
            while (scan.hasNextLine() && !nextLine.equals("#end")){
                choices = parseChoiceDefinitions(nextLine);
                choices.forEach(System.out::println);
                nextLine = scan.nextLine();
            }
        }
    }

    private List<String> parseChoiceDefinitions(String line) {
        Scanner scan = new Scanner(line);
        String name = scan.findInLine(Pattern.compile("<{2}[a-zæøåA-ZÆØÅ0-9 ]+>{2}"));
        List<String> choices = new ArrayList<>();
        choices.add(name);
        String choicesString = scan.findInLine(Pattern.compile("=[a-zæøåA-ZÆØÅ0-9 ,.@]+"));
        Arrays.stream(choicesString.substring(1).split(",")).forEach(item->{
            int index = 0;
            for(char a : item.toCharArray()) {
                if(a != ' ') {
                    break;
                } else {
                    index++;
                }
            }
            choices.add(item.substring(index));
        });
        return choices;
    }

    private String createEmailTemplateText(TemplateFile templateFile) throws FileNotFoundException {
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
