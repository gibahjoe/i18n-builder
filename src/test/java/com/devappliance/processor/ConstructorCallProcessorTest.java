package com.devappliance.processor;

import org.junit.jupiter.api.Test;
import spoon.Launcher;
import spoon.SpoonAPI;

/**
 * @author Gibah Joseph
 * Email: gibahjoe@gmail.com
 * Apr, 2020
 **/
class ConstructorCallProcessorTest {

    @Test
    void process() {
        SpoonAPI launcher = new Launcher();
        launcher.getEnvironment().setNoClasspath(true);
        launcher.addInputResource("./src/main/java/com/devappliance/processor/src/Pojo.java");
        launcher.setSourceOutputDirectory("./src/main/java");
        launcher.addProcessor(new ConstructorCallProcessor());
        launcher.run();
    }
}
