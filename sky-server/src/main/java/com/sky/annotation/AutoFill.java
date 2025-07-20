package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解 用于表示某个方法需要进行自动填充
 */
@Target(ElementType.METHOD) //该注解用于指定自定义注解的作用目标为方法（METHOD），表示该注解只能用在方法上。
@Retention(RetentionPolicy.RUNTIME) //该注解用于声明自定义注解的生命周期策略。 表示该注解在运行时依然有效，可以通过反射获取注解信息。
public @interface AutoFill {
    // 用于指定填充数据的操作类型 Insert、Update
    OperationType value();
}

