package com.depnjkt.example;

import com.depnjkt.annotation.Inject;
import com.depnjkt.annotation.InjectionType;

public class TestClass {

    // @Inject
    private Logger logger;

    /*
    @Inject
    public TestClass(Logger logger) {
        this.logger = logger;
    }
     */

    @Inject(InjectionType.SINGLETON)
    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public void log() {
        logger.log("Hello from TestClass");
    }

}
