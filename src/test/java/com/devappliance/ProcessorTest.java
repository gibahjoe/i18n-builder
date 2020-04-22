package com.devappliance;

import org.junit.jupiter.api.io.TempDir;

import java.io.File;

/**
 * @author Gibah Joseph
 * Email: gibahjoe@gmail.com
 * Apr, 2020
 **/
public interface ProcessorTest {

    void testThatCanProcessClass(@TempDir File outputDir);

    void testThatDoNotExtractAnnotationWorks();

    void testThatTypeExclusionWorks();
}
