package com.my.mvpframe.apt_lib.annotation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Create by jzhan on 2019/4/17
 */
public class ListenerInvocationHandler implements InvocationHandler {

    // 需要拦截的对象
    private Object target;
    // 需要拦截的方法集合
    private HashMap<String,Method> methodhMap = new HashMap<>();

    public ListenerInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (target != null) {
            // 获取需要拦截的方法名
            String methodName = method.getName();
            // 从集合中判断是否需要拦截
            method = methodhMap.get(methodName);
            // 方法存在则执行
            if (method != null) {
                if (method.getGenericParameterTypes().length == 0) {
                    // 这个地方是处理方法里面没有参数的情况
                    return method.invoke(target);
                }
                return method.invoke(target,args);
            }
        }
        return null;
    }
    /**
     * 将需要拦截的方法加入集合
     */
    public void addMethodMap(String methodName,Method method){
        methodhMap.put(methodName, method);
    }
}
