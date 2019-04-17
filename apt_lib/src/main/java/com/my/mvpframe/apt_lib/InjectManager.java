package com.my.mvpframe.apt_lib;

import android.app.Activity;
import android.view.View;

import com.my.mvpframe.apt_lib.annotation.ContentView;
import com.my.mvpframe.apt_lib.annotation.EventBase;
import com.my.mvpframe.apt_lib.annotation.InjectView;
import com.my.mvpframe.apt_lib.annotation.ListenerInvocationHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Create by jzhan on 2019/4/17
 *
 * 使用 InjectManager.inject(...)
 */
public class InjectManager {

    public static void inject(Activity activity) {
        // 布局注入
        injectLayout(activity);
        // 控件的注入
        injectViews(activity);
        // 事件的注入
        injectEvents(activity);
    }

    private static void injectLayout(Activity activity) {
        // 获取类
        Class<? extends Activity> clazz = activity.getClass();
        // 获取这个类上的注解
        ContentView contentView = clazz.getAnnotation(ContentView.class);
        if (contentView != null) {
            // 获取这个注解的值
            int layoutId = contentView.value();
            // 第一种方法
//            activity.setContentView(layoutId);
            // 第二种方法
            try {
                Method method = clazz.getMethod("setContentView", int.class);
                // 执行方法
                method.invoke(activity, layoutId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 控件的注入
     *
     * @param activity
     */
    private static void injectViews(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        // 获取类的每个属性
        Field[] fields = clazz.getDeclaredFields();
        // 循环，拿到每个属性
        for (Field field : fields) {
            // 获取每个属性上面的注解
            InjectView injectView = field.getAnnotation(InjectView.class);
            if (injectView != null) {
                int viewId = injectView.value();
                // 执行方法：findViewById
                try {
                    Method method = clazz.getMethod("findViewById", int.class);
                    Object view = method.invoke(activity, viewId);
                    // 将方法执行返回值View赋值给全局某属性
                    field.setAccessible(true);// 设置private访问权限
                    field.set(activity, view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 事件的注入
    private static void injectEvents(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        // 获取类的所有方法
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                // 获取注解类型
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if (annotationType != null) {
                    // 通过EventBase 获取事件的三个规律
                    EventBase eventBase = annotationType.getAnnotation(EventBase.class);
                    if (eventBase != null) {

                        String listenerSetter = eventBase.listenerSetter();
                        Class<?> listenerType = eventBase.listenerType();
                        String callBackListener = eventBase.callBackListener();

                        // 注解的值
                        try {
                            // 通过annotationType获取onClick注解的value值
                            Method valueMethod = annotationType.getDeclaredMethod("value");
                            // 执行value方法获得注解的值
                            int[] viewIds = (int[]) valueMethod.invoke(annotation);

                            // 打包之后，通过代理后续工作
                            ListenerInvocationHandler handler = new ListenerInvocationHandler(activity);
                            handler.addMethodMap(callBackListener, method);

                            Object listener = Proxy.newProxyInstance(listenerType.getClassLoader(), new Class[]{listenerType}, handler);

                            for (int viewId : viewIds) {
                                View view = activity.findViewById(viewId);
                                // 获取方法
                                Method setter = view.getClass().getMethod(listenerSetter, listenerType);
                                // 执行方法
                                setter.invoke(view, listener);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
