
![CircleCI](https://img.shields.io/circleci/build/github/gibahjoe/i18n-builder/master)
![Codecov](https://img.shields.io/codecov/c/github/gibahjoe/i18n-builder)
[![](https://jitpack.io/v/gibahjoe/i18n-builder.svg)](https://jitpack.io/#gibahjoe/i18n-builder)


i18n-builder is a java package that generates messages.properties from your source code.

### What does this package do?

This package is a simple tool that extracts the string literals in your java project, replaces them with a constant and then generates a messages.properties file containing key-value pairs for easy localisation.

## Installation

Add this to your build.gradle
```groovy
allprojects {
	repositories {
		maven { url 'https://jitpack.io' }
	}
}
```

add below dependency
```groovy
dependencies {
    implementation 'com.github.gibahjoe:i18n-builder:[latest version]'
    implementation 'com.github.gibahjoe:i18n-extractor-annotations:[latest version]'
}
```

### Latest version
| Package | version|
|---------|--------|
|i18n-builder|[![](https://jitpack.io/v/gibahjoe/i18n-builder.svg)](https://jitpack.io/#gibahjoe/i18n-builder)|
|i18n-extractor-annotation|[![](https://jitpack.io/v/gibahjoe/i18n-extractor-annotations.svg)](https://jitpack.io/#gibahjoe/i18n-extractor-annotations)|
 
## Usage
```java
public class App{
    public static void main(String[] args){
        // Source file location from where to find string literals (can be a directory)
        getExtractor().getConfig().addInputFolders("src/test/java/com/devappliance/i18nbuilder/processor/classprocessor/src/TestSource.java");
        
        //Fully qualified package name of constant that will hold the i18n keys
        getExtractor().getConfig().setTranslationKeyHolderClass("com.devappliance.i18nbuilder.generated.classprocessor.testThatCanProcessClass.Messages");
        
        //Where to save the updated sources. If you want it to save in same file, specify the root directory of your package
        getExtractor().getConfig().setSourceOutputDirectory("src/test/java");

        // Where to create the messages.properties file
        getExtractor().getConfig().setTranslationKeysOutputFile("src/test/resources/classprocessor/testThatCanProcessClass/messages.properties");
        
        //run extractor
        getExtractor().run();
    }
}
```
