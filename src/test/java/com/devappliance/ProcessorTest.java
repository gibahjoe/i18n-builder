package com.devappliance;

import org.junit.jupiter.api.io.TempDir;
import spoon.Launcher;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Gibah Joseph
 * Email: gibahjoe@gmail.com
 * Apr, 2020
 **/
public interface ProcessorTest {

    void testThatCanProcessClass(@TempDir File outputDir);

    void testThatDoNotExtractAnnotationWorks(@TempDir File outputDir);

    void testThatTypeExclusionWorks(@TempDir File outputDir);

    default Launcher getOutputLauncher(File outputDir) {
        System.out.println("====>v" + outputDir.getAbsolutePath());
        assertTrue(new File(outputDir.getAbsolutePath() + File.separator + "com/devappliance/i18nbuilder/processor/src/TestSource.java").exists());
        Launcher launcher = new Launcher();
        launcher.addInputResource(outputDir.getAbsolutePath() + File.separator + "com/devappliance/i18nbuilder");


        launcher.getEnvironment().setNoClasspath(true);
        launcher.getEnvironment().setAutoImports(true);

        launcher.setSourceOutputDirectory(outputDir);
        return launcher;
    }
}
