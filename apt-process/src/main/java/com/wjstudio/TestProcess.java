package com.wjstudio;

import com.example.AutoCreat;
import com.example.ZyaoAnnotation;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * 注解处理器
 * 完成注解的处理
 *  1.处理哪个注解
 *  2.如何处理
 */
//注册处理器，告知APT，AutoCrate是由TestProcess处理
@AutoService(Processor.class)
public class TestProcess extends AbstractProcessor {
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        //1. 定义需要处理的注解
        return Collections.singleton(AutoCreat.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //2. 如何处理该注解
        /**
          hello.java
          public final class HelloWorld {
             public static void main(String[] args) {
                    System.out.println("Hello, JavaPoet!");
                }
            }
         */
        //文件内容--java代码生成工具javapoet
        //MethodSpec:定义方法
        //TypeSec:定义类
        //JavaFile：生成.java文件
        MethodSpec main = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, "args")
                .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
                .build();

        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(main)
                .build();

        //构造java文件
        JavaFile javaFile = JavaFile.builder("com.songwenju.aptproject", helloWorld)
                .build();

        try {
            //java文件写在   APTProject-master/app/build/generated/source/apt/debug/com.songweiju.aptproject/HelloWorld.java
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(ZyaoAnnotation.class)) {
            //遍历annotatedElement 中的字段 和 方法
            for (Element element : annotatedElement.getEnclosedElements()) {
                System.out.println("---------TestProcess----------> element = "+element.getSimpleName()+" className = "+element.toString());
            }
        }

        return false;
    }
}
