package com.lucky.module_design.singleton;

/**
 * 懒汉式，线程安全====================================不建议使用
 *
 *这种方式具备很好的 lazy loading，能够在多线程中很好的工作，但是，效率很低(因为加锁)，99% 情况下不需要同步。
 */
public class SingletonLazySafe {
    private static SingletonLazySafe instance;

    private SingletonLazySafe() {
    }

    public static synchronized SingletonLazySafe getInstance() {
        if (instance == null) {
            instance = new SingletonLazySafe();
        }
        return instance;
    }
}