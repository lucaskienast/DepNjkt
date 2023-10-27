package com.depnjkt;

import com.depnjkt.annotation.Inject;
import com.depnjkt.annotation.InjectionType;
import com.depnjkt.config.ISyringe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class DepNjkt {

    private final ISyringe syringe;
    private final Map<Class<?>, Object> singletons = new HashMap<>();

    public DepNjkt(ISyringe syringe) {
        this.syringe = syringe;
        this.syringe.configure();
    }

    public static DepNjkt getInstance(ISyringe syringe) {
        return new DepNjkt(syringe);
    }

    public <T> T inject(Class<T> classToInjectTo) throws Exception {
        for (Constructor<?> constructor : classToInjectTo.getConstructors()) {
            if (constructor.isAnnotationPresent(Inject.class)) {
                return injectViaConstructor(constructor, classToInjectTo);
            }
        }

        for (Method method : classToInjectTo.getMethods()) {
            if (method.isAnnotationPresent(Inject.class)) {
                return injectViaSetters(classToInjectTo);
            }
        }

        return injectViaFields(classToInjectTo);
    }

    private <T> T injectViaSetters(Class<T> classToInjectTo) throws Exception {
        T instance = classToInjectTo.getConstructor().newInstance();

        for (Method method : classToInjectTo.getMethods()) {
            if (method.isAnnotationPresent(Inject.class)
                    && method.getName().startsWith("set")
                    && method.getParameterCount() == 1) {

                if (method.getAnnotation(Inject.class).value() == InjectionType.SINGLETON) {
                    method.invoke(instance, getSingleton(method.getParameterTypes()[0]));
                } else {
                    method.invoke(instance, inject(syringe.getInjectable(method.getParameterTypes()[0])));
                }
            }
        }

        return instance;
    }

    private Object getSingleton(Class<?> type) throws Exception {
        if (!singletons.containsKey(type)) {
            singletons.put(type, inject(syringe.getInjectable(type)));
        }
        return singletons.get(type);
    }

    private <T> T injectViaConstructor(Constructor<?> constructor, Class<T> classToInjectTo) throws Exception {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] dependencies = new Object[parameterTypes.length];

        int i = 0;
        for (Class<?> parameterType : parameterTypes) {
            dependencies[i++] = inject(syringe.getInjectable(parameterType));
        }

        return classToInjectTo.getConstructor(parameterTypes).newInstance(dependencies);
    }

    private <T> T injectViaFields(Class<T> classToInjectTo) throws Exception {
        T instance = classToInjectTo.getConstructor().newInstance();

        for (Field field : classToInjectTo.getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                field.setAccessible(true);
                field.set(instance, inject(syringe.getInjectable(field.getType())));
            }
        }

        return instance;
    }

}
