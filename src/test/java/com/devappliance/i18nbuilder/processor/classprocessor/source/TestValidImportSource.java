package com.devappliance.i18nbuilder.processor.classprocessor.source;

import com.devappliance.i18nbuilder.processor.classprocessor.source.other.AnAnnot;
import com.devappliance.i18nbuilder.processor.classprocessor.source.other.ReviewDto;
import com.devappliance.i18nbuilder.processor.classprocessor.source.other.ReviewerDTO;
import com.fasterxml.jackson.annotation.JsonAlias;

import javax.validation.constraints.NotNull;

/**
 * @author Gibah Joseph
 * email: gibahjoe@gmail.com
 * Apr, 2020
 **/
public class TestValidImportSource {
    public void testThatValidJavaxImportsAreGenerated(@NotNull(message = "message") @AnAnnot ReviewDto value, @JsonAlias ReviewerDTO normal) {

    }
}
