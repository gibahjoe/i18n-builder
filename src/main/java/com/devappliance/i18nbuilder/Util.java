package com.devappliance.i18nbuilder;

import com.devappliance.i18n.annotation.DoNotExtract;
import org.apache.commons.lang3.StringUtils;
import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.code.CtNewArray;
import spoon.reflect.declaration.*;
import spoon.reflect.factory.TypeFactory;
import spoon.reflect.path.CtRole;
import spoon.support.reflect.code.CtLiteralImpl;
import spoon.support.reflect.code.CtLocalVariableImpl;
import spoon.support.reflect.declaration.CtFieldImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author Gibah Joseph
 * Email: gibahjoe@gmail.com
 * Apr, 2020
 */
@DoNotExtract
public class Util {
    private static final List<String> alphabets = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
            "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
            "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z");
    private static final List<String> numbers = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");

    private final Extractor.Config config;
    private final Properties properties;


    public Util(Extractor.Config config, Properties properties) {
        this.config = config;
        this.properties = properties;
    }

    public CtField<String> createFieldInConstantClass(String stringLiteral, CtType<?> constantClass) {
        int i = 0;
        int stringKeyIterationIndex = 0;
        String stringKey = buildTranslationKey(stringLiteral, stringKeyIterationIndex);
        Object value = properties.get(stringKey);

        if (value != null && !value.equals(stringLiteral)) {
            while (value != null && !value.equals(stringLiteral)) {
                stringKey = buildTranslationKey(stringLiteral, ++stringKeyIterationIndex);
                value = properties.get(stringKey);
            }
        }
        String constantClassKeyFieldName = generateHolderClassKeyFieldName(stringLiteral, i);
        CtField<String> existingField = ((CtField<String>) (constantClass.getField(constantClassKeyFieldName)));
        while (existingField != null) {
            CtLiteral<String> valueByRole = existingField.getValueByRole(CtRole.ASSIGNMENT);
            if (stringKey.equals(valueByRole.getValue())) {
                return existingField;
            }
            constantClassKeyFieldName = generateHolderClassKeyFieldName(stringLiteral, ++i);
            existingField = ((CtField<String>) (constantClass.getField(constantClassKeyFieldName)));
        }
        CtField<String> argumentField = new CtFieldImpl<>();
        argumentField.addModifier(ModifierKind.PUBLIC);
        argumentField.addModifier(ModifierKind.STATIC);
        argumentField.addModifier(ModifierKind.FINAL);
        argumentField.setSimpleName(constantClassKeyFieldName);
        CtLiteral<String> ctExpression = new CtLiteralImpl<>();
        ctExpression.setValue(stringKey);
        argumentField.setAssignment(ctExpression);
        CtType<String> objectCtType = new TypeFactory().get(String.class);
        argumentField.setType(objectCtType.getReference());
        constantClass.addField(argumentField);
        return argumentField;
    }

    private String buildTranslationKey(String stringLiteral, int iteration) {
        StringBuilder keyBuilder = new StringBuilder();
        char[] charArray = stringLiteral.toCharArray();
        String separator = ".";
        for (int i = 0, charArrayLength = charArray.length; i < charArrayLength; i++) {
            char c = charArray[i];
            if (i == 0) {
                if (numbers.contains(String.valueOf(c))) {
                    appendSeparator(keyBuilder, separator);
                    keyBuilder.append(c);
                } else if (!alphabets.contains(String.valueOf(c))) {
                    appendSeparator(keyBuilder, separator);
                } else {
                    keyBuilder.append(c);
                }
            } else {
                if (!alphabets.contains(String.valueOf(c)) && !numbers.contains(String.valueOf(c))) {
                    appendSeparator(keyBuilder, separator);
                } else {
                    keyBuilder.append(c);
                }
            }
        }
        String key = keyBuilder.toString().trim();
        if (key.equals(separator)) {
            key = "";
        }
        if (StringUtils.isBlank(key)) {
            key = "translationKey";
        }
        if (key.startsWith(separator)) {
            key = "translationKey" + key;
        }
        if (key.endsWith(separator)) {
            key = key + "translationKey";
        }
        if (key.length() > 30) {
            key = key.substring(0, 30);
        }
        if (iteration > 0) {
            key = key + separator + iteration;
        }
        return key;
    }

    private StringBuilder appendSeparator(StringBuilder builder, String separator) {
        if (!builder.toString().endsWith(separator)) {
            return builder.append(separator);
        }
        return builder;
    }

    private String generateHolderClassKeyFieldName(String literalValue, int iteration) {
        StringBuilder fieldNameBuilder = new StringBuilder();
        char[] charArray = literalValue.toCharArray();
        String separator = "_";
        for (int i = 0, charArrayLength = charArray.length; i < charArrayLength; i++) {
            char c = charArray[i];
            if (i == 0) {
                if (numbers.contains(String.valueOf(c))) {
                    fieldNameBuilder.append("KEY");
                    fieldNameBuilder.append(c);
                } else if (!alphabets.contains(String.valueOf(c))) {
                    appendSeparator(fieldNameBuilder, separator);
                } else {
                    fieldNameBuilder.append(c);
                }
            } else {
                if (!alphabets.contains(String.valueOf(c)) && !numbers.contains(String.valueOf(c))) {
                    appendSeparator(fieldNameBuilder, separator);
                } else {
                    fieldNameBuilder.append(c);
                }
            }
        }

        String fieldName = fieldNameBuilder.toString().trim();
        if (fieldName.equals(separator)) {
            fieldName = "";
        }
        if (StringUtils.isBlank(fieldName)) {
            fieldName = "key";
        }
        if (fieldName.length() > 30) {
            fieldName = fieldName.substring(0, 30);
        }
        if (iteration > 0) {
            fieldName = fieldName + separator + iteration;
        }
        return fieldName.toUpperCase();
    }

    public boolean canExtract(CtLiteral<String> element) {
        if (element.getParent() instanceof CtLocalVariableImpl) {
            return true;
        } else if (element.getParent() instanceof CtField) {
            return true;
        } else if (element.getParent() instanceof CtConstructorCall) {
            CtConstructorCall<?> ctElement = (CtConstructorCall<?>) element.getParent();
            return !isExcludedType(ctElement.getType().getQualifiedName());
        } else if (element.getParent() instanceof CtAnnotation) {
            CtAnnotation<?> ctAnnotation = (CtAnnotation<?>) element.getParent();
            return !isExcludedType(ctAnnotation.getType().getQualifiedName());
        } else if (element.getParent() instanceof CtNewArray) {
            return true;
        } else
            return element.getParent() instanceof CtInvocation;

    }

    private boolean isExcludedType(String qualifiedName) {
        List<String> excludeTypeLiterals = config.getExcludeTypeLiterals();
        long exactTypeExclusionMatches = excludeTypeLiterals
                .stream().filter(s -> !s.endsWith(".*"))
                .filter(s -> s.equals(qualifiedName))
                .count();
        if (exactTypeExclusionMatches > 0) {
            return true;
        }
        long wildcardTypeExclusionMatches = excludeTypeLiterals
                .stream().filter(s -> s.endsWith(".*"))
                .map(s -> s.replace(".*", ""))
                .filter(s -> qualifiedName.startsWith(s))
                .count();
        return wildcardTypeExclusionMatches > 0;
    }

    public boolean hasDoNotExtractAnnotation(CtElement element) {
        if (element instanceof CtPackage) {
            return false;
        }
        if (element == null) {
            return false;
        }
        if (element.hasAnnotation(DoNotExtract.class)) {
            return true;
        }
        return hasDoNotExtractAnnotation(element.getParent());
    }

    public boolean isExcluded(CtAnnotation element) {

        return hasDoNotExtractAnnotation(element.getParent());
    }

    public void replace(CtLiteral<String> element, CtField<String> constantField) {

    }
}
