package com.depnjkt.config;

public interface ISyringe {

    void configure();

    <T> Class<? extends T> getInjectable(Class<T> type);

}
