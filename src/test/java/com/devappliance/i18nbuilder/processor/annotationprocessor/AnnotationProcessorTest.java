package com.devappliance.i18nbuilder.processor.annotationprocessor;

import com.devappliance.i18nbuilder.enums.ExtractionMode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

import static com.devappliance.i18nbuilder.Extractor.getExtractor;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Gibah Joseph
 * Email: gibahjoe@gmail.com
 * Apr, 2020
 **/
@Disabled
class AnnotationProcessorTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void process(@TempDir File outputDir) {
        getExtractor().getConfig().addInputFolders("src/test/java");
//        getExtractor().getConfig().addInputFolders("dental-door-integration-impl/src/main/java");
        getExtractor().getConfig().setTranslationKeyHolderClass("com.devappliance.generated.classprocessor.Messages");
        getExtractor().getConfig().setSourceOutputDirectory(outputDir.getAbsolutePath());
        getExtractor().getConfig()
                .setMode(ExtractionMode.ANNOTATED)
                .setTranslationKeysOutputFile("src/test/resources/classprocessor/messages.properties");

        getExtractor().run();

        assertTrue(new File(outputDir.getAbsolutePath() + File.separator + "com/devappliance/processor/annotationprocessor/src/TestSource.java").exists());
    }
}
