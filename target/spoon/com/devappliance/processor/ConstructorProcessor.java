package com.devappliance.processor;
/**
 *
 *
 * @author Gibah Joseph
Email: gibahjoe@gmail.com
Apr, 2020
 */
public class ConstructorProcessor extends spoon.processing.AbstractProcessor<spoon.reflect.code.CtConstructorCall<com.devappliance.processor.Pojo>> {
    @java.lang.Override
    public void process(spoon.reflect.code.CtConstructorCall<com.devappliance.processor.Pojo> element) {
        java.util.List<spoon.reflect.reference.CtTypeReference<?>> actualTypeArguments = element.getActualTypeArguments();
        actualTypeArguments.stream().forEach(( ctTypeReference) -> java.lang.System.out.println("===> ct" + ctTypeReference.getDeclaringType()));
    }
}