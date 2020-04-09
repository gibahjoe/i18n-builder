package com.devappliance.i18nbuilder.processor;

import com.devappliance.i18n.annotation.DoNotExtract;
import com.devappliance.i18nbuilder.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spoon.SpoonAPI;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtType;
import spoon.reflect.path.CtRole;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.ArrayList;
import java.util.List;

import static com.devappliance.i18nbuilder.Extractor.getExtractor;

/**
 * @author Gibah Joseph
 * Email: gibahjoe@gmail.com
 * Apr, 2020
 */
@DoNotExtract
public class ClassProcessor extends AbstractProcessor<CtType<?>> {
    static final Logger logger = LoggerFactory.getLogger(ClassProcessor.class);

    private CtType<?> messagesFile;

    private List<String> processedPackages = new ArrayList<>();

    private SpoonAPI launcher;

    public ClassProcessor(CtType<?> messagesFile, SpoonAPI launcher) {
        this.messagesFile = messagesFile;
        this.launcher = launcher;
    }

    @Override
    public boolean isToBeProcessed(CtType<?> candidate) {
        if (candidate.hasAnnotation(DoNotExtract.class)) {
            return false;
        }
        return true;
    }

    @Override
    public void process(CtType<?> element) {
        element.getQualifiedName();
        List<CtLiteral<String>> elements = element.getElements(new TypeFilter<CtLiteral<String>>(CtLiteral.class) {
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

    public List<String> getProcessedPackages() {
        return processedPackages;
    }
}
