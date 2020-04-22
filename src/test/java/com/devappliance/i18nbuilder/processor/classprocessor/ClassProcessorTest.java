package com.devappliance.i18nbuilder.processor.classprocessor;

import com.devappliance.ProcessorTest;
import com.devappliance.i18nbuilder.Extractor;
import com.devappliance.i18nbuilder.processor.classprocessor.source.TestSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import spoon.SpoonAPI;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.cu.CompilationUnit;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;

import java.io.File;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Gibah Joseph
 * Email: gibahjoe@gmail.com
 * Apr, 2020
 **/
class ClassProcessorTest implements ProcessorTest {

    private Extractor.Config config;
    private Extractor extractor;

    @BeforeEach
    void setUp(@TempDir File outputDir) {
        extractor = new Extractor();
        config = extractor.getConfig();
        config.setSourceOutputDirectory(outputDir.getAbsolutePath());
        config.setTranslationKeysOutputFile(outputDir.getAbsolutePath() + "/src/test/resources/classprocessor/testThatCanProcessClass/messages.properties");
    }

    @AfterEach
    void tearDown() {
        extractor = null;
    }

    @Test
    public void testThatCanProcessClass(@TempDir File outputDir) {
        config.addInputFolders("src/test/java/com/devappliance/i18nbuilder/processor/classprocessor/source/TestSource.java");
//        getExtractor().getConfig().addInputFolders("dental-door-integration-impl/src/main/java");
        config.setTranslationKeyHolderClass("com.devappliance.i18nbuilder.Messages");
        config.setSourceOutputDirectory(outputDir.getAbsolutePath());
        config.setTranslationKeysOutputFile(outputDir.getAbsolutePath() + "/src/test/resources/classprocessor/testThatCanProcessClass/messages.properties");
        SpoonAPI spoonAPI = extractor.run();

        assertTrue(new File(outputDir.getAbsolutePath() + File.separator + "com/devappliance/i18nbuilder/processor/classprocessor/source/TestSource.java").exists());
        assertTrue(new File(outputDir.getAbsolutePath() + File.separator + "com/devappliance/i18nbuilder/Messages.java").exists());
        assertTrue(new File(outputDir.getAbsolutePath() + File.separator + "src/test/resources/classprocessor/testThatCanProcessClass/messages.properties").exists());
    }

    @Override
    @Test
    @Disabled
    public void testThatDoNotExtractAnnotationWorks() {
        config.addInputFolders("src/test/java/com/devappliance/i18nbuilder/processor/classprocessor/source/TestSource.java");
//        getExtractor().getConfig().addInputFolders("dental-door-integration-impl/src/main/java");
        config.getExcludeTypeLiterals().add("java.lang.ArrayIndexOutOfBoundsException");
        config.getExcludeTypeLiterals().add("javax.validation.constraints.DecimalMin");
        SpoonAPI spoonAPI = extractor.run();

        CtType<TestSource> type = spoonAPI.getFactory().Type().get(TestSource.class);
        CompilationUnit compilationUnit = spoonAPI.getFactory().CompilationUnit().getOrCreate(type);

        assertEquals(1, compilationUnit.getImports().stream()
                .filter(ctImport -> ctImport.prettyprint().
                        equals("import com.devappliance.i18nbuilder.processor.classprocessor.source.other.ReviewDto;"))
                .count());
    }

    @Test
    public void testThatFieldLevelDoNotExtractAnnotationWorks() {
        config.addInputFolders("src/test/java/com/devappliance/i18nbuilder/processor/classprocessor/source/TestSource.java");
        SpoonAPI spoonAPI = extractor.run();

        System.out.println("===> output directory: " + config.getSourceOutputDirectory());

        CtType<TestSource> type = spoonAPI.getFactory().Type().get(TestSource.class);
        assertNotNull(type);
        List<CtLiteral<String>> ctLiterals = type.getElements(new TypeFilter<CtLiteral<String>>(CtLiteral.class) {
            @Override
            public boolean matches(CtLiteral<String> ctLiteral) {
                CtTypeReference<String> ctLiteralType = ctLiteral.getType();
                return super.matches(ctLiteral);
            }
        });

        assertEquals(1, ctLiterals.size());
    }

    @Test
    public void testThatTypeExclusionWorks() {
        config.addInputFolders("src/test/java/com/devappliance/i18nbuilder/processor/classprocessor/source/TestSource.java");

        config.getExcludeTypeLiterals().add("java.lang.ArrayIndexOutOfBoundsException");
        config.getExcludeTypeLiterals().add("javax.validation.constraints.DecimalMin");

        SpoonAPI spoonAPI = extractor.run();
        CtType<TestSource> type = spoonAPI.getFactory().Type().get(TestSource.class);

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
}
