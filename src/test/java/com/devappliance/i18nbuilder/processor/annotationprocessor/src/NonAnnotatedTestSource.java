package com.devappliance.i18nbuilder.processor.annotationprocessor.src;

import com.devappliance.i18nbuilder.processor.annotationprocessor.src.other.ReviewDto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Gibah Joseph
 * Email: gibahjoe@gmail.com
 * Apr, 2020
 **/
public class NonAnnotatedTestSource {
    private String firstName = "test for field literal";
    private String lastName;


    public NonAnnotatedTestSource(@NotBlank String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public NonAnnotatedTestSource setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public NonAnnotatedTestSource setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void testForConstructorLiterals() {
        NonAnnotatedTestSource t = new NonAnnotatedTestSource("first parameter", "second parameter");
    }

    public void testForExceptionInvocation() {
        throw new IllegalArgumentException("An Error has occurred");
    }

    public void testForSingleAnnotationLiteral(@NotBlank @SuppressWarnings("testForSingleAnnotationLiteral") String firstName) {
        throw new IllegalArgumentException("An Error has occurred");
    }

    public void testForArrayAnnotationLiteral(@SuppressWarnings({"testForSingleAnnotationLiteral", "secondtest case"}) String firstName) {
        throw new IllegalArgumentException("An Error has occurred");
    }

    public void testValidAnnotation(@Valid String firstName) {
        throw new IllegalArgumentException("An Error has occurred");
    }

    public void testThatValidAnnotationImportsAreCreated(@Valid @NotNull ReviewDto reviewDto) {
        throw new IllegalArgumentException("An Error has occurred");
    }
}
