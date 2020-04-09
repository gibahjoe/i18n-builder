package com.devappliance.processor;

import com.devappliance.processor.src.ErrorException;
import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtConstructorCall;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtLiteral;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtCompilationUnit;
import spoon.reflect.declaration.CtPackage;
import spoon.reflect.declaration.ModifierKind;
import spoon.support.reflect.declaration.CtClassImpl;
import spoon.support.reflect.declaration.CtCompilationUnitImpl;
import spoon.support.reflect.declaration.CtPackageImpl;

import java.io.File;
import java.util.List;

/**
 * @author Gibah Joseph
 * Email: gibahjoe@gmail.com
 * Apr, 2020
 **/
public class ConstructorCallProcessor extends AbstractProcessor<CtConstructorCall<ErrorException>> {


    @Override
    public void process(CtConstructorCall<ErrorException> element) {
        CtPackage pack = new CtPackageImpl();
        pack.setSimpleName("com.devappliance");

        CtClass ctClass = new CtClassImpl();
        pack.getTypes().add(ctClass);
        ctClass.setParent(pack);
        ctClass.setVisibility(ModifierKind.PUBLIC);
        ctClass.setSimpleName("Messages");

        CtCompilationUnit cu = new CtCompilationUnitImpl();
        cu.setDeclaredPackage(pack);
        cu.addDeclaredType(ctClass);
        String fn = cu.getDeclaredTypes().get(0).getQualifiedName().replaceAll("\\.", File.separator) + ".java";
        System.out.println(fn);
//        CtFieldReference<String> cfr = new CtFieldReferenceImpl();
//        cfr.setSimpleName("TEST");
//        cfr.setDeclaringType(CtTypeReferenceImpl.Messages.class.getTypeName());
        List<CtExpression<?>> arguments = element.getArguments();
        if (arguments.size() > 0) {
            CtExpression<?> argument = arguments.get(0);
            if (argument instanceof CtLiteral) {
                System.out.println(((CtLiteral) argument).getValue());
                element.removeArgument(argument);
            } else {
                System.out.println((argument).getClass());
            }
        }

    }
}
