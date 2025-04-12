import com.squareup.javapoet.*;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.*;
import java.util.Arrays;

public class PoetTest {

    public static final String packageName = "org.example";

    @Test
    public void testHello() throws IOException {
        MethodSpec methodSpec = MethodSpec
                .methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, "args")
                .addStatement("$T.out.println($S)", System.class, "Hello World!!!")
                .build();


        TypeSpec typeSpec = TypeSpec.classBuilder("HelloWorld")
                .addMethod(methodSpec)
                .addModifiers(Modifier.PUBLIC)
                .build();

        JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
                .addFileComment("made by jack\n")
                .addFileComment("this file is generated automatically. Do not modify!!!")
                .build();
        javaFile.writeTo(System.out);

        javaFile.writeToFile(new File("src/main/java"));
    }

    @Test
    public void testPerson() throws IOException {
        MethodSpec constructMethod = MethodSpec
                .constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String.class, "name")
                .addParameter(int.class, "age")
                .addStatement("this.$N = $N", "name", "name")
                .addStatement("this.$N = $N", "age", "age")
                .build();

        MethodSpec getNameMethod = MethodSpec
                .methodBuilder("getName")
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class)
                .addStatement("return this.name")
                .build();

        TypeSpec typeSpec = TypeSpec.classBuilder("Person")
                .addModifiers(Modifier.PUBLIC)
                .addField(FieldSpec.builder(String.class, "name", Modifier.PRIVATE, Modifier.FINAL).build())
                .addField(FieldSpec.builder(int.class, "age", Modifier.PRIVATE, Modifier.FINAL).build())
                .addMethod(constructMethod)
                .addMethod(getNameMethod)
                .addStaticBlock(CodeBlock.builder()
                        .addStatement("$T.out.println($S)", System.class, "Hello Poet")
                        .build())
                .build();


        JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
                .addFileComment("made by jack\n")
                .addFileComment("this file is generated automatically. Do not modify!!!")
                .build();
        javaFile.writeTo(System.out);

        javaFile.writeToFile(new File("src/main/java"));
    }

    @Test
    public void testInterface() throws IOException {

        MethodSpec methodSpec = MethodSpec
                .methodBuilder("print")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(void.class)
                .build();

        MethodSpec methodSpec2 = MethodSpec
                .methodBuilder("connect")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(String.class, "name")
                .addParameter(int.class, "age")
                .returns(String.class)
                .build();

        TypeSpec typeSpec = TypeSpec.interfaceBuilder("AA")
                .addModifiers(Modifier.PUBLIC)
                .addMethods(Arrays.asList(methodSpec, methodSpec2))
                .addField(FieldSpec.builder(String.class, "CHANNEL", Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                        .initializer("$S", "connection")
                        .build()
                ).build();


        JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
                .addFileComment("made by jack\n")
                .addFileComment("this file is generated automatically. Do not modify!!!")
                .build();
        javaFile.writeTo(System.out);
        javaFile.writeToFile(new File("src/main/java"));
    }

    @Test
    public void testEnum() throws IOException {

        TypeSpec typeSpec = TypeSpec.enumBuilder("COLOR")
                .addModifiers(Modifier.PUBLIC)
                .addEnumConstant("RED", TypeSpec.anonymousClassBuilder("$S", "FF0000")
                        .addMethod(MethodSpec.methodBuilder("toString")
                                .addAnnotation(Override.class)
                                .addModifiers(Modifier.PUBLIC)
                                .addStatement("return $S", "RED!")
                                .returns(String.class).build())
                        .build())
                .addEnumConstant("BLUE", TypeSpec.anonymousClassBuilder("$S", "00FF00").build())
                .addEnumConstant("GREEN", TypeSpec.anonymousClassBuilder("$S", "0000FF").build())
                .addField(FieldSpec.builder(String.class, "rgb", Modifier.PRIVATE).build())
                .addMethod(MethodSpec.constructorBuilder()
                        .addParameter(String.class, "rgb")
                        .addStatement("this.$N = $N", "rgb", "rgb")
                        .build())
                .build();

        JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
                .indent("  ")
                .addFileComment("made by jack\n")
                .addFileComment("this file is generated automatically. Do not modify!!!")
                .build();
        javaFile.writeTo(System.out);
        javaFile.writeToFile(new File("src/main/java"));
    }

    @Test
    public void testAnnotation() throws IOException {

        MethodSpec m1 = MethodSpec
                .methodBuilder("sequence")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .defaultValue("{}")
                .returns(String[].class)
                .build();

        MethodSpec m2 = MethodSpec
                .methodBuilder("write")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .defaultValue("true")
                .returns(boolean.class)
                .build();

        MethodSpec m3 = MethodSpec
                .methodBuilder("extract")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .defaultValue("false")
                .returns(boolean.class)
                .build();

        TypeSpec typeSpec = TypeSpec.annotationBuilder("Permission")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(Retention.class)
                        .addMember("value", "$T.RUNTIME", RetentionPolicy.class)
                        .build())
                .addAnnotation(AnnotationSpec.builder(Target.class)
                        .addMember("value", "{$T.TYPE, $T.ANNOTATION_TYPE}", ElementType.class, ElementType.class)
                        .build())
                .addAnnotation(AnnotationSpec.builder(Inherited.class).build())
                .addMethod(m1)
                .addMethod(m2)
                .addMethod(m3)
                .build();

        JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
                .addFileComment("made by jack\n")
                .addFileComment("this file is generated automatically. Do not modify!!!")
                .build();
        javaFile.writeTo(System.out);
        javaFile.writeToFile(new File("src/main/java"));
    }


}
