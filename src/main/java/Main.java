import model.EmailTemplate;
import services.TemplateService;

import java.io.File;
import java.net.URL;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        String path = main.getFileFromResources("templates/mailTemplate.txt").getPath();
        TemplateService templateService = new TemplateService();
        EmailTemplate emailTemplate = templateService.parseEmailTemplateFile(path);
        System.out.println(emailTemplate.toString());
        System.out.println("Fields: ");
        emailTemplate.getTemplateFields().forEach((k,v)-> System.out.println(v.toString()));
    }

    private File getFileFromResources(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("File not found!");
        } else {
            return new File(resource.getFile());
        }
    }
}
