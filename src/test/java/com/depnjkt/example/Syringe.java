package com.depnjkt.example;

import com.depnjkt.config.ISyringe;

import java.util.HashMap;
import java.util.Map;

public class Syringe implements ISyringe {

    private Map<Class<?>, Class<?>> injectables = new HashMap<>();

    @Override
    public void configure() {
        registerInjectable(Logger.class, TestLogger.class);
    }

    private <T> void registerInjectable(Class<T> baseClass, Class<? extends T> subClass) {
        injectables.put(baseClass, subClass.asSubclass(baseClass));
    }

    @Override
    public <T> Class<? extends T> getInjectable(Class<T> type) {
        Class<?> injectable = injectables.get(type);

        if (injectable == null) {
            throw new IllegalArgumentException("No injectable registered for type: " + type);
        }

        return injectable.asSubclass(type);
    }
}
