package com.wjstudio;

import com.example.Autowired;
import com.example.ZyaoAnnotation;
import com.google.auto.service.AutoService;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

@AutoService(Processor.class)
public class AutowiredProcess extends AbstractProcessor{


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(Autowired.class)) {
            TypeElement typeElement = (TypeElement) annotatedElement.getEnclosingElement();
            /*
            annotatedElement.getEnclosingElement() 得到 annotatedElement的 父元素

            -----cxk---> simpleName = MainActivity qualiyName = com.songwenju.aptproject.MainActivity
            -----cxk---> simpleName = MainActivity qualiyName = com.songwenju.aptproject.MainActivity
             */
            System.out.println("-----cxk---> simpleName = "+typeElement.getSimpleName() +" qualiyName = "+typeElement.getQualifiedName()
            +" ");
        }
        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        //支持的注解
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(Autowired.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
