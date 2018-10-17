package com.wjstudio;

import com.example.ZyaoAnnotation;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

//demo 描述地址：https://www.jianshu.com/p/07ef8ba80562
@AutoService(Processor.class)
public class MyProcessor extends AbstractProcessor{

    //Types 一个用来处理TypeMirror的工具类
    private Types mTypeUtils;
    //一个用来处理Element的工具类，源代码的每一个部分都是一个特定类型的Element
    private Elements mElementUtils;
    //Filer可以创建文件
    private Filer mFiler;
    //Messager为注解处理器提供了一种报告错误消息，警告信息和其他消息的方式
    //Messager是用来给那些使用了你的注解处理器的第三方开发者显示信息的
    private Messager mMessager;

    //每一个注解处理器类都必须有一个空的构造函数。然而，这里有一个特殊的init()方法，它会被注解处理工具调用，并输入ProcessingEnviroment参数。ProcessingEnviroment提供很多有用的工具类Elements,Types和Filer
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        //初始化我们需要的基础工具
        mTypeUtils = processingEnv.getTypeUtils();
        mElementUtils = processingEnv.getElementUtils();
        mFiler = processingEnv.getFiler();
        mMessager = processingEnv.getMessager();

    }

    //这相当于每个处理器的主函数main()。你在这里写你的扫描、评估和处理注解的代码，以及生成Java文件。输入参数RoundEnviroment，可以让你查询出包含特定注解的被注解元素
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        System.out.println("--------MyProcessor ---->"+ roundEnv.processingOver());
        //--------MyProcessor ---->false
        //--------MyProcessor ---->false
        //--------MyProcessor ---->true

        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(ZyaoAnnotation.class)) {

            //请记住：Element可以是类、方法、变量等。所以，接下来，我们必须检查这些Element是否是一个类
            if (annotatedElement.getKind() != ElementKind.CLASS) {
                error(annotatedElement, "Only classes can be annotated with %s", ZyaoAnnotation.class.getSimpleName());
                // 退出处理
                return true;
            }

            //解析，并生成代码
            analysisAnnotated(annotatedElement);
        }
        return false;
    }

    //这里你必须指定，这个注解处理器是注册给哪个注解的。注意，它的返回值是一个字符串的集合，包含本处理器想要处理的注解类型的合法全称
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        //支持的注解
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(ZyaoAnnotation.class.getCanonicalName());
        return annotations;
    }

    //用来指定你使用的Java版本。通常这里返回SourceVersion.latestSupported()。然而，如果你有足够的理由只支持Java 7的话，你也可以返回SourceVersion.RELEASE_7。我推荐你使用前者
    @Override
    public SourceVersion getSupportedSourceVersion() {
        //支持java版本
        return SourceVersion.latestSupported();
    }

    private static final String SUFFIX = "$$ZYAO";
    private void analysisAnnotated(Element classElement) {//这里可以用JavaPoet来生成.java文件

        ZyaoAnnotation annotation = classElement.getAnnotation(ZyaoAnnotation.class);
        String name = annotation.name();
        String text = annotation.text();

        String newClassName = name + SUFFIX;

        StringBuilder builder = new StringBuilder()
                .append("package com.zyao89.demoprocessor.auto;\n\n")
                .append("public class ")
                .append(newClassName)
                .append(" {\n\n")
                .append("\tpublic String getMessage() {\n")
                .append("\t\treturn \"");

        builder.append(text).append(name);

        builder.append("\";\n").append("\t}\n").append("}\n");

        try {
            JavaFileObject source = mFiler.createSourceFile("com.zyao89.demoprocessor.auto."+newClassName);
            Writer writer = source.openWriter();
            writer.write(builder.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void error(Element e, String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
    }
}
