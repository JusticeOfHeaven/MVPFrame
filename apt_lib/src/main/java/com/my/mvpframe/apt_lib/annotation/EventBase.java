package com.my.mvpframe.apt_lib.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Create by jzhan on 2019/4/17
 */
@Target(ElementType.ANNOTATION_TYPE)// 作用在注解之上
@Retention(RetentionPolicy.RUNTIME)
public @interface EventBase {
    // setXxxlistener
    String listenerSetter();

    // new View.OnXxxlistener
    Class<?> listenerType();

    // 回调执行方法  OnXxx()
    String callBackListener();
}
