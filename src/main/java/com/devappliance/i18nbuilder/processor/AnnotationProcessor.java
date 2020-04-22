package com.devappliance.i18nbuilder.processor;

import com.devappliance.i18n.annotation.DoNotExtract;
import com.devappliance.i18n.annotation.Extract;
import com.devappliance.i18nbuilder.Extractor;
import spoon.SpoonAPI;
import spoon.processing.AbstractAnnotationProcessor;
import spoon.reflect.code.CtFieldAccess;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtType;
import spoon.reflect.path.CtRole;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

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
    private Extractor extractor;

    public AnnotationProcessor(CtType<?> messagesFile, SpoonAPI launcher, Extractor extractor) {
        this.messagesFile = messagesFile;
        this.launcher = launcher;
        this.extractor = extractor;
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
                if (extractor.getUtil().hasDoNotExtractAnnotation(ctLiteral)) {
                    return false;
                }
                if (!extractor.getUtil().canExtract(ctLiteral)) {
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
            CtField<String> fieldInConstantClass = extractor.getUtil().createFieldInConstantClass(literalValue, messagesFile);
            CtLiteral<String> literalVariableKey = fieldInConstantClass.getValueByRole(CtRole.ASSIGNMENT);
            extractor.addProperty(literalVariableKey.getValue(), literalValue);

            CtFieldReference<String> ctFieldReference = getFactory().Field().createReference(fieldInConstantClass);
            ctFieldReference.setStatic(true);
            CtFieldAccess<String> fieldRead = getFactory().Core().createFieldRead();
            fieldRead.setVariable(ctFieldReference);
            stringCtLiteral.replace(fieldRead);
            modified = true;
        }
        if (modified) {
            processedPackages.add(element.getQualifiedName());
        }
    }
}
