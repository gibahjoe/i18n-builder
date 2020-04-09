package com.devappliance.i18nbuilder.processor.src;

import com.devappliance.i18n.annotation.Extract;
import com.devappliance.i18nbuilder.processor.src.other.ReviewDto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Gibah Joseph
 * Email: gibahjoe@gmail.com
 * Apr, 2020
 **/
@Extract
public class TestSource {
    private String firstName = "test for field literal";
    private String lastName;


    public TestSource(@NotBlank String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public TestSource setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public TestSource setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void testForConstructorLiterals() {
        TestSource t = new TestSource("first parameter", "second parameter");
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
