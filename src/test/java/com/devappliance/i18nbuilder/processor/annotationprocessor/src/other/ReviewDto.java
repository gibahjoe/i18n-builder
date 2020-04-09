package com.devappliance.i18nbuilder.processor.annotationprocessor.src.other;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ReviewDto {
    private String comment;
    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    private int rating;
    private ReviewerDTO reviewer;
    private String entityName;
    private Long entityId;

}
