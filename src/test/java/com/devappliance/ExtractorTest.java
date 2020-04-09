package com.devappliance;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import spoon.SpoonAPI;
import spoon.reflect.visitor.DefaultJavaPrettyPrinter;
import spoon.reflect.visitor.PrettyPrinter;

import java.io.File;

import static com.devappliance.i18nbuilder.Extractor.getExtractor;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Gibah Joseph
 * Email: gibahjoe@gmail.com
 * Apr, 2020
 **/
@Disabled
class ExtractorTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testThatCanUseDefaultJavaPrettyPrinter(@TempDir File outputDir) {
        getExtractor().getConfig().addInputFolders("src/test/java/com/devappliance/processor/src/TestSource.java");
//        getExtractor().getConfig().addInputFolders("dental-door-integration-impl/src/main/java");
        getExtractor().getConfig().setTranslationKeyHolderClass("com.devappliance.generated.classprocessor.Messages");
        getExtractor().getConfig().setSourceOutputDirectory(outputDir.getAbsolutePath());
        getExtractor().getConfig().setTranslationKeysOutputFile("src/test/resources/classprocessor/messages.properties");
        getExtractor().getConfig().setRewriteEntireClass(true);
        SpoonAPI launcher = getExtractor().run();

        PrettyPrinter prettyPrinter = launcher.getEnvironment().createPrettyPrinter();

        assertTrue(prettyPrinter instanceof DefaultJavaPrettyPrinter);
        assertTrue(new File(outputDir.getAbsolutePath() + File.separator + "com/devappliance/processor/annotationprocessor/src/TestSource.java").exists());
    }

    @Test
    void testThatCanUseSniperJavaPrettyPrinter(@TempDir File outputDir) {
        getExtractor().getConfig().addInputFolders("src/test/java/com/devappliance/processor/src/TestSource.java");
//        getExtractor().getConfig().addInputFolders("dental-door-integration-impl/src/main/java");
        getExtractor().getConfig().setTranslationKeyHolderClass("com.devappliance.generated.classprocessor.Messages");
        getExtractor().getConfig().setSourceOutputDirectory(outputDir.getAbsolutePath());
        getExtractor().getConfig().setTranslationKeysOutputFile("src/test/resources/classprocessor/messages.properties");
        getExtractor().getConfig().setRewriteEntireClass(false);
        SpoonAPI launcher = getExtractor().run();

        PrettyPrinter prettyPrinter = launcher.getEnvironment().createPrettyPrinter();

        assertTrue(prettyPrinter instanceof DefaultJavaPrettyPrinter);
        assertTrue(new File(outputDir.getAbsolutePath() + File.separator + "com/devappliance/processor/annotationprocessor/src/TestSource.java").exists());
    }

    @Test
    void testThatCorrectImportsAreGenerated() {
        getExtractor().getConfig().addInputFolders("src/test/java/com/devappliance/processor/src/TestSource.java");
//        getExtractor().getConfig().addInputFolders("dental-door-integration-impl/src/main/java");
        getExtractor().getConfig().setTranslationKeyHolderClass("com.devappliance.generated.classprocessor.Messages");
        getExtractor().getConfig().setSourceOutputDirectory("src/test/resources/testThatCorrectImportsAreGenerated");
        getExtractor().getConfig().setTranslationKeysOutputFile("src/test/resources/classprocessor/messages.properties");
        getExtractor().getConfig().setRewriteEntireClass(true);
        SpoonAPI launcher = getExtractor().run();

        PrettyPrinter prettyPrinter = launcher.getEnvironment().createPrettyPrinter();

        assertTrue(prettyPrinter instanceof DefaultJavaPrettyPrinter);
        assertTrue(new File("src/test/resources/testThatCorrectImportsAreGenerated" + File.separator + "com/devappliance/processor/annotationprocessor/src/TestSource.java").exists());
    }
}
