# Template Generator


1. [Overview](#overview)
2. [Syntax](#syntax)
3. [Example](#example)


<a name="overview"></a>
### Overview
Template content generator that reads template files and creates a gui to help fill out the data

Its main purpose is email, but it could be used for any template. It requires a basic syntax to create a template, description at the bottom.

#### Main template
<img width="1002" alt="Screenshot 2020-03-19 at 14 42 21" src="https://user-images.githubusercontent.com/21195947/77079377-d9c6b500-69ef-11ea-9af9-19aceb65d337.png">

#### Settings
Used for creating values for fields that go across multiple templates
<img width="600" alt="Screenshot 2020-03-19 at 14 39 15" src="https://user-images.githubusercontent.com/21195947/77079428-f367fc80-69ef-11ea-86fa-55e9664ad3a2.png">

<a name="syntax"></a>
### Template File Syntax:

#### One lined text field:
Gives a small 1 line text field
```<<fieldName>>```

#### Multi lined text field:
Gives a large multi lined text field
```<!largeField!>```

#### MultiChoiceFields are defined on top of file:
Defined within the ```#def``` and ```#endef``` field at the top of the file
```
#def
<<multiChoiceField>>=many, choices
#enddef
```

#### Fields predefined in settings, available across multiple templates:
Defined in the settings menu, and will automatically insert the value into
the template when it exists.
```<$fieldFromConfigStorage$> ```

#### Adding hotkeys for copy specific fields quickly
Works with most single button inputs.
Defined within the ```#def``` and ```#endef``` field at the top of the file
```
#def
*F1=<!content!>
*F2=<<day>>
#enddef
````
<a name="example"></a>
#### Example
```
#def
<<day>>=Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday
*F1=<!content!>
*F2=<<day>>
#enddef
Hi I'm <$nameField$>,

<!content!>

I'd love a reply before <<day>>!

Regards,

<$nameField$>
```
