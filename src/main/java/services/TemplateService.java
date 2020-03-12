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

    public EmailTemplate parseEmailTemplateFile(TemplateFile file) throws FileNotFoundException {

        String templateText = createEmailTemplateText(file);
        String cleanTemplateText = cleanTemplateText(templateText);
        // Choice definitions
        Map<String, List<String>> choiceMap = new HashMap<>();
        if(hasDefinitions(templateText)) {
            choiceMap = getDefinitionsFromTemplateText(templateText);
        }
        // Handle fields - standard, large, permanent, multi-fields
        Map<String, TemplateTextField> templateTextFieldMap = parseTemplateFields(cleanTemplateText, choiceMap);
        // TODO: Permanent fields
        EmailTemplate emailTemplate = new EmailTemplate(file.getPath(), file.getName(), cleanTemplateText, templateTextFieldMap);
        emailTemplate.setChoiceDefinitions(choiceMap);
        return emailTemplate;
    }

    private Map<String, TemplateTextField> templateTextFieldMap = new HashMap<>();
    private Map<String, TemplateTextField> parseTemplateFields(String templateText, Map<String, List<String>> choiceDefinitions) {

        // Standard fields & Choice fields
        parseStandardAndChoiceFields(templateText, choiceDefinitions);
        // Large fields

        // Permanent fields

        // Multi-fields

        return templateTextFieldMap;
    }

    private void parseStandardAndChoiceFields(String templateText, Map<String, List<String>> choiceDefinitions) {
        Scanner scan = new Scanner(templateText);

        scan.findAll(Pattern.compile("<{2}[a-zæøåA-ZÆØÅ0-9 ]+>{2}"))
                .forEach(item -> {
                    if (choiceDefinitions.containsKey(item.group())) {
                        if(Objects.isNull(templateTextFieldMap.get(item.group()))){
                            TemplateTextField templateTextField = new TemplateTextField(
                                    item.end() - item.start(),
                                    item.group(),
                                    TemplateTextField.FieldType.CHOICE_FIELD,
                                    choiceDefinitions.get(item.group()));
                            templateTextFieldMap.put(item.group(), templateTextField);
                        }
                    } else if(templateTextFieldMap.get(item.group()) == null){
                        TemplateTextField templateTextField = new TemplateTextField(
                                item.end() - item.start(),
                                item.group(),
                                TemplateTextField.FieldType.STANDARD_FIELD);
                        templateTextField.addLocation(item.start());
                        templateTextFieldMap.put(item.group(), templateTextField);
                    }
                    templateTextFieldMap.get(item.group()).addLocation(item.start());
                });
        scan.close();
    }

    private boolean hasDefinitions(String templateText) {
        Scanner scan = new Scanner(templateText);
        boolean hasDefinition = scan.nextLine().contains("#def");
        scan.close();
        return hasDefinition;
    }

    private Map<String, List<String>> getDefinitionsFromTemplateText(String templateText) {
        Scanner scan = new Scanner(templateText);
        String nextLine = scan.nextLine();
        Map<String, List<String>> choiceMap = new HashMap<>();
        while (scan.hasNextLine() && !nextLine.contains("#enddef")) {
            if(!nextLine.contains("#def")) {
                Scanner lineScanner = new Scanner(nextLine);
                String name = lineScanner.findInLine(Pattern.compile("<{2}[a-zæøåA-ZÆØÅ0-9 ]+>{2}"));
                choiceMap.put(name, parseChoiceDefinitions(nextLine));
            }
            nextLine = scan.nextLine();
        }
        scan.close();
        return choiceMap;
    }

    private List<String> parseChoiceDefinitions(String line) {
        Scanner scan = new Scanner(line);
        List<String> choices = new ArrayList<>();
        String choicesString = scan.findInLine(Pattern.compile("=[a-zæøåA-ZÆØÅ0-9 ,.@]+"));
        Arrays.stream(choicesString.substring(1).split(",")).forEach(item -> {
            int index = 0;
            for (char a : item.toCharArray()) {
                if (a != ' ') {
                    break;
                } else {
                    index++;
                }
            }
            choices.add(item.substring(index));
        });
        scan.close();
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
        scan.close();
        return emailStringBuilder.toString();
    }

    private String cleanTemplateText(String templateText) {
        Scanner scan = new Scanner(templateText);
        StringBuilder stringBuilder = new StringBuilder();
        boolean afterDefinition = false;
        while (scan.hasNextLine()) {
            if (afterDefinition) {
                stringBuilder.append(scan.nextLine());
                if (scan.hasNextLine()) {
                    stringBuilder.append("\n");
                }
            } else if (scan.nextLine().contains("#end")) {
                afterDefinition = true;
            }
        }
        scan.close();
        return stringBuilder.toString();
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
}
