# Template Generator
Template content generator that reads template files and creates a gui to help fill out the data

Its main purpose is email, but it could be used for any template. It requires a basic syntax to create a template, description at the bottom.

#### Main template
<img width="1002" alt="Screenshot 2020-03-19 at 14 42 21" src="https://user-images.githubusercontent.com/21195947/77079377-d9c6b500-69ef-11ea-9af9-19aceb65d337.png">

#### Settings
Used for creating values for fields that go across multiple templates
<img width="600" alt="Screenshot 2020-03-19 at 14 39 15" src="https://user-images.githubusercontent.com/21195947/77079428-f367fc80-69ef-11ea-86fa-55e9664ad3a2.png">


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

#### Example
```
#def
<<day>>=Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday
#enddef
Hi I'm <$nameField$>,

<!content!>

I'd love a reply before <<day>>!

Regards,

<$nameField$>
```
