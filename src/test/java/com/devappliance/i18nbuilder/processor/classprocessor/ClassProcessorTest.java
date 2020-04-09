package com.devappliance.i18nbuilder.processor.classprocessor;

import com.devappliance.i18n.annotation.DoNotExtract;
import com.devappliance.i18nbuilder.processor.classprocessor.src.TestSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import spoon.SpoonAPI;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.declaration.CtType;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;

import java.io.File;
import java.util.Collection;
import java.util.List;

import static com.devappliance.i18nbuilder.Extractor.getExtractor;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Gibah Joseph
 * Email: gibahjoe@gmail.com
 * Apr, 2020
 **/
class ClassProcessorTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testThatCanProcessClass(@TempDir File outputDir) {
        getExtractor().getConfig().addInputFolders("src/test/java/com/devappliance/i18nbuilder/processor/classprocessor/src/TestSource.java");
//        getExtractor().getConfig().addInputFolders("dental-door-integration-impl/src/main/java");
        getExtractor().getConfig().setTranslationKeyHolderClass("com.devappliance.i18nbuilder.generated.classprocessor.testThatCanProcessClass.Messages");
        getExtractor().getConfig().setSourceOutputDirectory(outputDir.getAbsolutePath());
        getExtractor().getConfig().setTranslationKeysOutputFile("src/test/resources/classprocessor/testThatCanProcessClass/messages.properties");
        SpoonAPI spoonAPI = getExtractor().run();
        Collection<CtType<?>> allTypes = spoonAPI.getModel().getAllTypes();

        CtType<?> type = allTypes.stream().filter(ctType -> ctType.getSimpleName().equals(TestSource.class.getSimpleName())).findFirst().orElse(null);

        assertNotNull(type);
        List<CtLiteral<String>> elements = type.getElements(new TypeFilter<CtLiteral<String>>(CtLiteral.class) {
            @Override
            public boolean matches(CtLiteral<String> ctLiteral) {
                CtTypeReference<String> ctLiteralType = ctLiteral.getType();
                if (ctLiteral.hasAnnotation(DoNotExtract.class)) {
                    return false;
                }
                if (ctLiteralType == null) {
                    return false;
                }
                if (!String.class.getSimpleName().equals(ctLiteralType.getSimpleName())) {
                    return false;
                }
                return super.matches(ctLiteral);
            }
        });
        assertTrue(elements.isEmpty());
        assertTrue(new File(outputDir.getAbsolutePath() + File.separator + "com/devappliance/i18nbuilder/processor/classprocessor/src/TestSource.java").exists());
    }
}
