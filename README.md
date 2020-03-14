# EmailGenerator
Email content generator that reads template files and creates a gui to help fill out the data

Its main purpose is email, but it could be used for any template. It requires a basic syntax to create a template, description at the bottom.

<img width="850" alt="Screenshot 2020-03-13 at 23 56 01" src="https://user-images.githubusercontent.com/21195947/76670031-5425ae00-6586-11ea-8c38-2861323db90a.png">



### Template File Syntax:

#### MultiChoiceFields are defined on top of file:
```
#def
<<multiChoiceField>>=many, choices
#enddef
```

#### One lined text field:
```<<fieldName>>```

#### Multi lined text field:
```<!largeField!>```

#### Fields predefined in settings, available across multiple templates:
```<$fieldFromConfigStorage$> ```
