package com.devappliance.i18nbuilder.processor.classprocessor.source.other;

import javax.validation.constraints.NotBlank;

public class ReviewerDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String email;
    private String phoneNumber;

}
