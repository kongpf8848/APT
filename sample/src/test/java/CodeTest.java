import com.squareup.javapoet.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Headers;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class CodeTest {

    public static final String packageName = "org.example";

    @Test
    public void code_01() throws IOException {
        int from = 1;
        int to = 10;
        String op = "*";
        MethodSpec methodSpec = MethodSpec
                .methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, "args")
                .addStatement("int result = 1")
                .beginControlFlow("for (int i = $L; i < $L; i++)", from, to)
                .addStatement("result = result $L i ", op)
                .endControlFlow()
                .addStatement("System.out.println(result)")
                .build();


        ClassName string = ClassName.get("java.lang", "String");
        ClassName list = ClassName.get("java.util", "List");
        ClassName arrayList = ClassName.get("java.util", "ArrayList");
        TypeName listOfString = ParameterizedTypeName.get(list, string);
        MethodSpec beyondMethod = MethodSpec.methodBuilder("beyond")
                .returns(ParameterizedTypeName.get(List.class, String.class))
                .addStatement("$T result = new $T<>()", listOfString, arrayList)
                .addStatement("result.add(new $T($S))", string, "Java")
                .addStatement("result.add(new $T($S))", string, "C++")
                .addStatement("result.add(new $T($S))", string, "Python")
                .addStatement("return result")
                .build();

        MethodSpec fooMethod = MethodSpec.methodBuilder("foo")
                .returns(String.class)
                .addStatement("int i = 10")
                .beginControlFlow("if(i<0)")
                .addStatement("return \"bad\"")
                .nextControlFlow("else if(i<60)")
                .addStatement("return \"normal\"")
                .nextControlFlow("else if(i<90)")
                .addStatement("return \"good\"")
                .nextControlFlow("else")
                .addStatement("return \"haha\"")
                .endControlFlow()
                .build();

        MethodSpec k1 = MethodSpec.methodBuilder("k1")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(int.class)
                .addStatement("int num = 10")
                .beginControlFlow("try")
                .addStatement("num += 10")
                .addStatement("return num")
                .nextControlFlow("catch ($T e)", Exception.class)
                .addStatement("throw new $T(e)", RuntimeException.class)
                .nextControlFlow("finally")
                .addStatement("num += 10")
                .addStatement("return num")
                .endControlFlow()
                .build();

        MethodSpec hexDigit = MethodSpec.methodBuilder("hexDigit")
                .addParameter(int.class, "i")
                .returns(char.class)
                .addStatement("return (char) (i < 10 ? i + '0' : i - 10 + 'a')")
                .build();

        MethodSpec byteToHex = MethodSpec.methodBuilder("byteToHex")
                .addParameter(int.class, "b")
                .returns(String.class)
                .addStatement("char[] result = new char[2]")
                .addStatement("result[0] = $N((b >>> 4) & 0xf)", hexDigit)
                .addStatement("result[1] = $N(b & 0xf)", hexDigit)
                .addStatement("return new String(result)")
                .build();

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("food", "tacos");
        map.put("count", 3);

        MethodSpec toStringMethod = MethodSpec.methodBuilder("toString")
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class)
                .addAnnotation(Override.class)
                .addAnnotation(AnnotationSpec.builder(SuppressWarnings.class)
                        .addMember("value", "$S", "only")
                        .build())
                .addStatement("return $S", "Hoverboard")
                .build();

        TypeSpec typeSpec = TypeSpec.classBuilder("HelloWorld")
                .addMethod(methodSpec)
                .addMethod(fooMethod)
                .addMethod(beyondMethod)
                .addMethod(k1)
                .addMethod(hexDigit)
                .addMethod(byteToHex)
                .addMethod(toStringMethod)
                .addModifiers(Modifier.PUBLIC)
                .build();


        JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
                .addFileComment("made by jack\n")
                .addFileComment("this file is generated automatically. Do not modify!!!")
                .build();
        javaFile.writeTo(System.out);

        javaFile.writeToFile(new File("src/main/java"));
    }
}
