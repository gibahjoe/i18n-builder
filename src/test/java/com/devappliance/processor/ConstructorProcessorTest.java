package com.devappliance.processor;


import org.junit.jupiter.api.Test;
import spoon.Launcher;
import spoon.SpoonAPI;


/**
 * @author Gibah Joseph
 * Email: gibahjoe@gmail.com
 * Apr, 2020
 **/
public class ConstructorProcessorTest {
    @Test
    void process() {
        SpoonAPI launcher = new Launcher();
        launcher.getEnvironment().setNoClasspath(true);
        launcher.addInputResource("./src/main/java/com/devappliance/processor/src/Pojo.java");
        launcher.setSourceOutputDirectory("./src/main/java");
        launcher.addProcessor(new ConstructorProcessor());
        launcher.run();

//        final CtType<Pojo> target = launcher.getFactory().Type().get(Pojo.class);
//        final CtMethod<?> m = target.getCMethodsByName("m").get(0);
//
//        assertTrue(m.getBody().getStatements().size() >= 2);
//       assertTrue(m.getBody().getStatement(0) instanceof CtIf);
//        assertTrue(m.getBody().getStatement(1) instanceof CtIf);
    }
}
