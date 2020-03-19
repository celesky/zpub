package com.celesky.zpub.common.enums;

import java.lang.annotation.*;

/**
 * @desc:
 * @author: panqiong
 * @date: 2019-02-25
 */
@Target(ElementType.METHOD) //目标对象是类型
@Retention(RetentionPolicy.RUNTIME) //保存至运行时
@Documented //生成javadoc文档时，该注解内容一起生成文档
@Inherited //该注解被子类继承
public @interface MyAnnotation {

    /**
     * 当只有一个元素时，建议元素名定义为value(),这样使用时赋值可以省略"value="
     * @return
     */
    public String value() default "";

    String name() default "devin";

    int age() default 18;

    boolean isStudent() default true;

    String[] alias();

    enum Color {GREEN, BLUE, RED,}

    Color favoriteColor() default Color.GREEN;
}
