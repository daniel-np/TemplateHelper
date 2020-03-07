import model.EmailTemplate;
import services.TemplateService;

public class Main {
    public static void main(String[] args) {
        String path = "/Users/Daniel/Documents/java/Emailgenerator/src/main/resources/fmtemplates/mailTemplate.txt";
        TemplateService templateService = new TemplateService();
        EmailTemplate emailTemplate = templateService.parseEmailTemplate(path);
        System.out.println(emailTemplate.toString());
        System.out.println("Fields: ");
        emailTemplate.getTemplateFields().forEach((k,v)-> System.out.println(v.toString()));
    }
}
