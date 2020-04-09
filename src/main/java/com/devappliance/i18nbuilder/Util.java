package com.devappliance.i18nbuilder;

import com.devappliance.i18n.annotation.DoNotExtract;
import org.apache.commons.lang3.StringUtils;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtType;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.factory.Factory;
import spoon.reflect.factory.TypeFactory;
import spoon.reflect.path.CtRole;
import spoon.support.reflect.code.CtLiteralImpl;
import spoon.support.reflect.code.CtLocalVariableImpl;
import spoon.support.reflect.declaration.CtFieldImpl;

import java.util.Arrays;
import java.util.List;

import static com.devappliance.i18nbuilder.Extractor.getExtractor;

/**
 * @author Gibah Joseph
 * Email: gibahjoe@gmail.com
 * Apr, 2020
 */
@DoNotExtract
public class Util {
    private static List<String> alphabets = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
    private static List<String> numbers = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
    private Factory factory;

    public Util(Factory factory) {
        this.factory = factory;
    }

    public static CtField<String> createFieldInConstantClass(String stringLiteral, CtType<?> constantClass) {
        int i = 0;
        int stringKeyIterationIndex = 0;
        String stringKey = getkeyForLiteral(stringLiteral, stringKeyIterationIndex);
        Object value = getExtractor().getProperties().get(stringKey);

        if (value != null && !value.equals(stringLiteral)) {
            while (value != null && !value.equals(stringLiteral)) {
                stringKey = getkeyForLiteral(stringLiteral, ++stringKeyIterationIndex);
                value = getExtractor().getProperties().get(stringKey);
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

    private static String getkeyForLiteral(String stringLiteral, int iteration) {
        StringBuilder keyBuilder = new StringBuilder();
        char[] charArray = stringLiteral.toCharArray();
        for (int i = 0, charArrayLength = charArray.length; i < charArrayLength; i++) {
            char c = charArray[i];
            if (i == 0) {
                if (numbers.contains(String.valueOf(c))) {
                    keyBuilder.append(".");
                    keyBuilder.append(c);
                } else if (!alphabets.contains(String.valueOf(c).toUpperCase())) {
                    keyBuilder.append(".");
                } else {
                    keyBuilder.append(c);
                }
            } else {
                if (!alphabets.contains(String.valueOf(c).toUpperCase()) && !numbers.contains(String.valueOf(c))) {
                    keyBuilder.append(".");
                } else {
                    keyBuilder.append(c);
                }
            }
        }
        String key = keyBuilder.toString().trim();
        if (StringUtils.isBlank(key)) {
            key = "key";
        }
        if (key.startsWith(".")) {
            key = "key" + key;
        }
        if (key.endsWith(".")) {
            key = key + "key";
        }
        if (key.length() > 20) {
            key = key.substring(0, 20);
        }
        if (iteration > 0) {
            key = key + "." + iteration;
        }
        return key.toLowerCase();
    }

    private static String generateHolderClassKeyFieldName(String literalValue, int iteration) {
        StringBuilder fieldNameBuilder = new StringBuilder();
        char[] charArray = literalValue.toCharArray();
        for (int i = 0, charArrayLength = charArray.length; i < charArrayLength; i++) {
            char c = charArray[i];
            if (i == 0) {
                if (numbers.contains(String.valueOf(c))) {
                    fieldNameBuilder.append("KEY");
                    fieldNameBuilder.append(c);
                } else if (!alphabets.contains(String.valueOf(c).toUpperCase())) {
                    fieldNameBuilder.append("_");
                } else {
                    fieldNameBuilder.append(c);
                }
            } else {
                if (!alphabets.contains(String.valueOf(c).toUpperCase()) && !numbers.contains(String.valueOf(c))) {
                    fieldNameBuilder.append("_");
                } else {
                    fieldNameBuilder.append(c);
                }
            }
        }

        String fieldName = fieldNameBuilder.toString().trim();
        if (StringUtils.isBlank(fieldName)) {
            fieldName = "key";
        }
        if (fieldName.length() > 20) {
            fieldName = fieldName.substring(0, 20);
        }
        if (iteration > 0) {
            fieldName = fieldName + "_" + iteration;
        }
        return fieldName.toUpperCase();
    }

    public void replace(CtLiteral<String> element, CtField<String> constantField) {
        if (shouldExtract(element)) {
            element.replace(convertFieldToExpression(constantField));
        }
    }

    private boolean shouldExtract(CtLiteral<String> element) {
        if (element.getParent() instanceof CtLocalVariableImpl) {
            return true;
        } else if (element.getParent() instanceof CtField) {
            return true;
        } else if (element.getParent() instanceof CtConstructorCall) {
            return true;
        } else if (element.getParent() instanceof CtAnnotation) {
            CtAnnotation<?> ctAnnotation = (CtAnnotation<?>) element.getParent();
            return !getExtractor().getConfig().getExcludeAnnotationLiterals()
                    .contains(ctAnnotation.getAnnotationType()
                            .getQualifiedName());
        } else if (element.getParent() instanceof CtNewArray) {
            return true;
        } else
            return element.getParent() instanceof CtInvocation;

    }

    public CtExpression convertFieldToExpression(CtField<String> argumentField) {
        return factory.createCodeSnippetExpression((((argumentField.getDeclaringType().getPackage().getQualifiedName() + ".") + argumentField.getDeclaringType().getSimpleName()) + ".") + argumentField.getSimpleName());
    }
}
