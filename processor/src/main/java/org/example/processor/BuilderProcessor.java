package org.example.processor;


import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import com.squareup.javapoet.*;
import org.example.annotation.Builder;

@AutoService(Processor.class)
@SupportedAnnotationTypes("org.example.annotation.Builder")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class BuilderProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Builder.class);
        for (Element element : elements) {
            if (element.getKind() == ElementKind.CLASS) {
                TypeElement typeElement = (TypeElement) element;
                try {
                    generateBuilderFile(typeElement);
                } catch (IOException e) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
                }
            }
        }
        return true;
    }

    private void generateBuilderFile(TypeElement typeElement) throws IOException {
        String className = typeElement.getSimpleName().toString();
        System.out.println("className:" + className);
        String builderClassName = className + "Builder";
        System.out.println("builderClassName:" + builderClassName);
        String packageName = processingEnv.getElementUtils().getPackageOf(typeElement).toString();
        System.out.println("packageName:" + packageName);

        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(builderClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);


        List<VariableElement> fields = ElementFilter.fieldsIn(typeElement.getEnclosedElements());
        for (Element e : fields) {
            System.out.println("kind:" + e.getKind() + "-" + e.getSimpleName() + "," + e.asType());
        }

        StringBuilder parameters = new StringBuilder();
        for (VariableElement field : fields) {
            String fieldName = field.getSimpleName().toString();
            parameters.append(fieldName + ",");
            typeBuilder.addField(FieldSpec.builder(TypeName.get(field.asType()), fieldName)
                    .addModifiers(Modifier.PRIVATE)
                    .build());
            typeBuilder.addMethod(MethodSpec.methodBuilder(fieldName)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(TypeName.get(field.asType()), fieldName)
                    .returns(ClassName.get(packageName, builderClassName))
                    .addStatement("this.$N = $N", fieldName, fieldName)
                    .addStatement("return this")
                    .build());
        }

        MethodSpec buildMethod = MethodSpec.methodBuilder("build")
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.get(packageName, className))
                .addStatement("return new $T($L)", ClassName.get(packageName, className), parameters.isEmpty() ? "" : parameters.substring(0, parameters.length() - 1))
                .build();
        typeBuilder.addMethod(buildMethod);

        JavaFile javaFile = JavaFile.builder(packageName, typeBuilder.build())
                .addFileComment("made by jack\n")
                .addFileComment("this file is generated automatically. Do not modify!!!")
                .build();
        javaFile.writeTo(System.out);
        javaFile.writeToFile(new File("src/main/java"));
    }
}
