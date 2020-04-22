package com.devappliance.i18nbuilder.processor.classprocessor.source;

import com.devappliance.i18n.annotation.DoNotExtract;
import com.devappliance.i18nbuilder.processor.classprocessor.source.other.ReviewDto;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Gibah Joseph
 * Email: gibahjoe@gmail.com
 * Apr, 2020
 **/
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

    @DoNotExtract
    public void testMethodExclusionAnnotation(@Valid String firstName) {
        throw new IllegalArgumentException("testMethodExclusionAnnotation");
    }

    public void testThatValidAnnotationImportsAreCreated(@Valid @NotNull ReviewDto reviewDto) {
        throw new IllegalArgumentException("An Error has occurred");
    }

    public void testTypeLiteralsExclusion(@DecimalMin("testthatnotextracted") int val) {
        throw new ArrayIndexOutOfBoundsException("testTypeLiteralsExclusion");
    }
}
