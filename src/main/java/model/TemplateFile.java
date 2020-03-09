package model;

import java.io.File;
import java.net.URI;

public class TemplateFile extends File {
    public TemplateFile(String s) {
        super(s);
    }

    public TemplateFile(String s, String s1) {
        super(s, s1);
    }

    public TemplateFile(File file, String s) {
        super(file, s);
    }

    public TemplateFile(URI uri) {
        super(uri);
    }

    @Override
    public String toString() {
        String fileName = super.getName();
        for(int i = fileName.length()-1; i > 0; i--) {
            if(fileName.charAt(i) == '.') {
                fileName = fileName.substring(0,i);
                break;
            }
        }
        return fileName;
    }
}
