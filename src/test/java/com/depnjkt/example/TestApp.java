package com.depnjkt.example;

import com.depnjkt.DepNjkt;

public class TestApp {

    public static void main(String[] args) throws Exception {
        DepNjkt depNjkt = DepNjkt.getInstance(new Syringe());

        TestClass testClass = depNjkt.inject(TestClass.class);
        testClass.log();

        TestClass testClass2 = depNjkt.inject(TestClass.class);
        testClass2.log();
    }

}
