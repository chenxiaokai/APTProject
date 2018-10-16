package com.wjstudio.factory;

import com.example.Factory;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;

public class FactoryAnnotatedClass {

    private TypeElement annotatedClassElement;
    private String qualifiedGroupClassName;
    private String simpleFactoryGroupName;
    private String id;

    public FactoryAnnotatedClass(TypeElement classElement) {
        this.annotatedClassElement = classElement;

        Factory annotation = classElement.getAnnotation(Factory.class);
        id = annotation.id();

        if ("".equals(id)) {
            throw new IllegalArgumentException(
                    String.format(
                            "id() in @%s for class %s is null or empty! that's not allowed",
                            Factory.class.getSimpleName(),
                            classElement.getQualifiedName().toString()));
        }

        /*
            这有点棘手，因为这里的类型是java.lang.Class。那意味着，这是一个真实的Class对象。因为注解处理器在编译 java 源码之前执行，所以我们必须得考虑两种情况：
         */
        try {
            /*
             1. 这个类已经被编译过了：这种情况是第三方 .jar 包含已编译的被@Factory注解 .class 文件。这种情况下，我们可以像try 代码块中所示那样直接获取Class。
               (译注：因为@Factory的@Retention为RetentionPolicy.CLASS，所有被编译过的代码也会保留@Factory的注解信息)
             */
            Class<?> clazz = annotation.type();
            qualifiedGroupClassName = clazz.getCanonicalName();
            simpleFactoryGroupName = clazz.getSimpleName();
        } catch (MirroredTypeException mte) {

            /*
            2. 这个类还没有被编译：这种情况是我们尝试编译被@Fractory注解的源代码。这种情况下，直接获取Class会抛出MirroredTypeException异常。
               幸运的是，MirroredTypeException包含一个TypeMirror，它表示我们未被编译类。因为我们知道它一定是一个Class类型（我们前面有检查过），
               所以我们可以将它转换为DeclaredType， 然后获取TypeElement来读取合法名称。
             */

            DeclaredType classTypeMirror = (DeclaredType) mte.getTypeMirror();
            TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
            qualifiedGroupClassName = classTypeElement.getQualifiedName().toString();
            simpleFactoryGroupName = classTypeElement.getSimpleName().toString();
        }

        System.out.println("-----------> qualifiedGroupClassName = "+qualifiedGroupClassName+"  simpleFactoryGroupName = "+simpleFactoryGroupName);
        //-----------> qualifiedGroupClassName = com.songwenju.aptproject.pizza.Meal  simpleFactoryGroupName = Meal
        //-----------> qualifiedGroupClassName = com.songwenju.aptproject.pizza.Meal  simpleFactoryGroupName = Meal
        //-----------> qualifiedGroupClassName = com.songwenju.aptproject.pizza.Meal  simpleFactoryGroupName = Meal
    }

    public String getId() {
        return id;
    }

    public String getQualifiedFactoryGroupName() {
        return qualifiedGroupClassName;
    }

    public String getSimpleFactoryGroupName() {
        return simpleFactoryGroupName;
    }

    public TypeElement getTypeElement() {
        return annotatedClassElement;
    }
}
