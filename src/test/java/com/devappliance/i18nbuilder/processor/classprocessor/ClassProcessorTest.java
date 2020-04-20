package com.devappliance.i18nbuilder.processor.classprocessor;

import com.devappliance.ProcessorTest;
import com.devappliance.i18nbuilder.Util;
import com.devappliance.i18nbuilder.processor.classprocessor.src.TestSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import spoon.Launcher;
import spoon.SpoonAPI;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static com.devappliance.i18nbuilder.Extractor.getExtractor;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Gibah Joseph
 * Email: gibahjoe@gmail.com
 * Apr, 2020
 **/
class ClassProcessorTest implements ProcessorTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void testThatCanProcessClass(@TempDir File outputDir) {
        getExtractor().getConfig().addInputFolders("src/test/java/com/devappliance/i18nbuilder/processor/src/TestSource.java");
//        getExtractor().getConfig().addInputFolders("dental-door-integration-impl/src/main/java");
        getExtractor().getConfig().setTranslationKeyHolderClass("com.devappliance.i18nbuilder.generated.classprocessor.testThatCanProcessClass.Messages");
        getExtractor().getConfig().setSourceOutputDirectory(outputDir.getAbsolutePath());
        getExtractor().getConfig().setTranslationKeysOutputFile("src/test/resources/classprocessor/testThatCanProcessClass/messages.properties");
        SpoonAPI spoonAPI = getExtractor().run();

        assertTrue(new File(outputDir.getAbsolutePath() + File.separator + "com/devappliance/i18nbuilder/processor/src/TestSource.java").exists());
    }

    @Test
    public void testThatDoNotExtractAnnotationWorks(@TempDir File outputDir) {
        getExtractor().getConfig().addInputFolders("src/test/java/com/devappliance/i18nbuilder/processor/src/TestSource.java");
//        getExtractor().getConfig().addInputFolders("dental-door-integration-impl/src/main/java");
        getExtractor().getConfig().setTranslationKeyHolderClass("com.devappliance.i18nbuilder.generated.classprocessor.testThatCanProcessClass.Messages");
        getExtractor().getConfig().setSourceOutputDirectory(outputDir.getAbsolutePath());
//        getExtractor().getConfig().getExcludeTypeLiterals().add("java.lang.ArrayIndexOutOfBoundsException");
//        getExtractor().getConfig().getExcludeTypeLiterals().add("javax.validation.constraints.DecimalMin");
        getExtractor().getConfig().setTranslationKeysOutputFile("src/test/resources/classprocessor/testThatCanProcessClass/messages.properties");
        SpoonAPI spoonAPI = getExtractor().run();

        CtType<?> type = getOutputType(outputDir);

        assertNotNull(type);
        List<CtLiteral<String>> elements = type.getElements(new TypeFilter<CtLiteral<String>>(CtLiteral.class) {
            @Override
            public boolean matches(CtLiteral<String> ctLiteral) {
                CtTypeReference<String> ctLiteralType = ctLiteral.getType();
                return Util.hasDoNotExtractAnnotation(ctLiteral);
            }
        });
        assertEquals(1, elements.size());
    }

    @Test
    public void testThatTypeExclusionWorks(@TempDir File outputDir) {
        getExtractor().getConfig().addInputFolders("src/test/java/com/devappliance/i18nbuilder/processor/src/TestSource.java");
//        getExtractor().getConfig().addInputFolders("dental-door-integration-impl/src/main/java");
        getExtractor().getConfig().setTranslationKeyHolderClass("com.devappliance.i18nbuilder.generated.classprocessor.testThatCanProcessClass.Messages");
        getExtractor().getConfig().setSourceOutputDirectory(outputDir.getAbsolutePath());
        getExtractor().getConfig().getExcludeTypeLiterals().add("java.lang.ArrayIndexOutOfBoundsException");
        getExtractor().getConfig().getExcludeTypeLiterals().add("javax.validation.constraints.DecimalMin");
        getExtractor().getConfig().setTranslationKeysOutputFile("src/test/resources/classprocessor/testThatCanProcessClass/messages.properties");
        SpoonAPI spoonAPI = getExtractor().run();

        CtType<?> type = getOutputType(outputDir);

        assertNotNull(type);
        List<CtLiteral<String>> elements = type.getElements(new TypeFilter<CtLiteral<String>>(CtLiteral.class) {
            @Override
            public boolean matches(CtLiteral<String> ctLiteral) {
                CtTypeReference<String> ctLiteralType = ctLiteral.getType();
                return super.matches(ctLiteral);
            }
        });
        assertEquals(3, new HashSet<>(elements).size());
    }

    private CtType<?> getOutputType(File outputDir) {
        System.out.println("====>v" + outputDir.getAbsolutePath());
        assertTrue(new File(outputDir.getAbsolutePath() + File.separator + "com/devappliance/i18nbuilder/processor/src/TestSource.java").exists());
        Launcher launcher = new Launcher();
        launcher.addInputResource(outputDir.getAbsolutePath() + File.separator + "com/devappliance/i18nbuilder");


        launcher.getEnvironment().setNoClasspath(true);
        launcher.getEnvironment().setAutoImports(true);

        launcher.setSourceOutputDirectory(outputDir);
        Collection<CtType<?>> allTypes = launcher.buildModel().getAllTypes();
        return allTypes.stream().filter(ctType -> ctType.getSimpleName().equals(TestSource.class.getSimpleName())).findFirst().orElse(null);
    }
}
