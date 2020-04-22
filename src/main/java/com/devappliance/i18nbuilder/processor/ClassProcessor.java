package com.devappliance.i18nbuilder.processor;

import com.devappliance.i18n.annotation.DoNotExtract;
import com.devappliance.i18nbuilder.Extractor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spoon.SpoonAPI;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtFieldAccess;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtType;
import spoon.reflect.path.CtRole;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.ArrayList;
import java.util.List;

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
    private Extractor extractor;

    public ClassProcessor(CtType<?> messagesFile, SpoonAPI launcher, Extractor extractor) {
        this.messagesFile = messagesFile;
        this.launcher = launcher;
        this.extractor = extractor;
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
        List<String> excludePackages = extractor.getConfig().getExcludePackages();
        String formattedPackageExclude = excludePackages.stream().map(s -> {
            if (s.endsWith(".*")) {
                return s.replace(".*", "");
            }
            return s;
        }).filter(s -> element.getQualifiedName().startsWith(s)).findAny().orElse(null);
        if (!StringUtils.isBlank(formattedPackageExclude)) {
            return;
        }
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

    public List<String> getProcessedPackages() {
        return processedPackages;
    }
}
