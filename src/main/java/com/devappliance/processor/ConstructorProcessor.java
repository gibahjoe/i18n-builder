package com.devappliance.processor;

import com.devappliance.processor.src.Pojo;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtConstructor;

/**
 * @author Gibah Joseph
 * Email: gibahjoe@gmail.com
 * Apr, 2020
 **/
public class ConstructorProcessor extends AbstractProcessor<CtConstructor<Pojo>> {


    @Override
    public void process(CtConstructor<Pojo> element) {
        element.getParameters().stream().forEach(ctParameter -> System.out.println(ctParameter.getSimpleName()));
    }
}
