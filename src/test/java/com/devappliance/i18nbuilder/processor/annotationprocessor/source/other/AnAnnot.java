package com.devappliance.i18nbuilder.processor.annotationprocessor.source.other;

import javax.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Gibah Joseph
 * email: gibahjoe@gmail.com
 * Apr, 2020
 **/
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})//, TYPE_USE
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {})
public @interface AnAnnot {
}
