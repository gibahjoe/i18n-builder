package com.devappliance.i18nbuilder.processor;

import com.devappliance.i18n.annotation.DoNotExtract;
import com.devappliance.i18n.annotation.Extract;
import com.devappliance.i18nbuilder.Util;
import spoon.SpoonAPI;
import spoon.processing.AbstractAnnotationProcessor;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtType;
import spoon.reflect.path.CtRole;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import static com.devappliance.i18nbuilder.Extractor.getExtractor;

/**
 * @author Gibah Joseph
 * Email: gibahjoe@gmail.com
 * Apr, 2020
 */
@DoNotExtract
public class AnnotationProcessor extends AbstractAnnotationProcessor<Extract, CtClass<?>> {
    private CtType<?> messagesFile;

    private List<String> processedPackages = new ArrayList<>();

    private SpoonAPI launcher;

    public AnnotationProcessor(CtType<?> messagesFile, SpoonAPI launcher) {
        this.messagesFile = messagesFile;
        this.launcher = launcher;
    }

    @Override
    public boolean shoudBeConsumed(CtAnnotation<? extends Annotation> annotation) {
        return false;
    }

    public List<String> getProcessedPackages() {
        return processedPackages;
    }

    @Override
    public void process(Extract annotation, CtClass element) {
        element.getQualifiedName();
        List<CtLiteral<String>> elements = element.getElements(new TypeFilter<CtLiteral<String>>(CtLiteral.class) {
            @Override
            public boolean matches(CtLiteral<String> ctLiteral) {
                CtTypeReference<String> ctLiteralType = ctLiteral.getType();
                if (Util.hasDoNotExtractAnnotation(ctLiteral)) {
                    return false;
                }
                if (!Util.canExtract(ctLiteral)) {
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
        boolean modified = false;
        for (CtLiteral<String> stringCtLiteral : elements) {
            String literalValue = stringCtLiteral.getValue();
            CtField<String> fieldInConstantClass = Util.createFieldInConstantClass(literalValue, messagesFile);
            CtLiteral<String> literalVariableKey = fieldInConstantClass.getValueByRole(CtRole.ASSIGNMENT);
            getExtractor().addProperty(literalVariableKey.getValue(), literalValue);
            new Util(getFactory()).replace(stringCtLiteral, fieldInConstantClass);
            modified = true;
        }
        if (modified) {
            processedPackages.add(element.getQualifiedName());
        }
    }
}
