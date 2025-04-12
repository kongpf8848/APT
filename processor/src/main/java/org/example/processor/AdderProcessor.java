package org.example.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.example.annotation.Adder;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.Set;

@AutoService(Processor.class)
@SupportedAnnotationTypes("org.example.annotation.Adder")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class AdderProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Adder.class)) {
            if (element.getKind() != ElementKind.METHOD) {
                continue;
            }

            ExecutableElement method = (ExecutableElement) element;
            Adder adder = method.getAnnotation(Adder.class);

            int num1 = adder.num1();
            int num2 = adder.num2();
            String className = ((TypeElement) method.getEnclosingElement()).getQualifiedName().toString();
            generateCode(className + "Gen", method.getSimpleName().toString(), num1, num2);
        }
        return true;
    }

    private void generateCode(String className, String methodName, int a, int b) {
        try {
            MethodSpec methodSpec = MethodSpec.methodBuilder(methodName)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(void.class)
                    .addStatement("$T.out.println(\"Result: $L\")", System.class, (a + b))
                    .build();

            ClassName clazz = ClassName.get(getPackage(className), getSimpleClassName(className));
            TypeSpec typeSpec = TypeSpec.classBuilder(clazz)
                    .addMethod(methodSpec)
                    .build();
            JavaFile javaFile = JavaFile.builder(getPackage(className), typeSpec)
                    .addFileComment("made by jack\n")
                    .addFileComment("This codes are generated automatically. Do not modify!")
                    .build();
            javaFile.writeTo(processingEnv.getFiler());

        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
    }

    private String getPackage(String className) {
        return className.contains(".") ? className.substring(0, className.lastIndexOf('.')) : "";
    }

    private String getSimpleClassName(String className) {
        return className.contains(".") ?
                className.substring(className.lastIndexOf('.') + 1) : className;
    }
}