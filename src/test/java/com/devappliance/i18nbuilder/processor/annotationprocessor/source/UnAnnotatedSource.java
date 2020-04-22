package com.devappliance.i18nbuilder.processor.annotationprocessor.source;

import com.devappliance.i18nbuilder.processor.annotationprocessor.source.other.AnAnnot;
import com.devappliance.i18nbuilder.processor.annotationprocessor.source.other.ReviewDto;
import com.devappliance.i18nbuilder.processor.annotationprocessor.source.other.ReviewerDTO;
import com.fasterxml.jackson.annotation.JsonAlias;

import javax.validation.constraints.NotNull;

/**
 * @author Gibah Joseph
 * email: gibahjoe@gmail.com
 * Apr, 2020
 **/

public class UnAnnotatedSource {
    public void testThatValidJavaxImportsAreGenerated(@NotNull(message = "message") @AnAnnot ReviewDto value, @JsonAlias ReviewerDTO normal) {

    }
}
