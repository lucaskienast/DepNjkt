package com.depnjkt.example;

public class TestLogger implements Logger {

    private int callCount = 0;

    @Override
    public void log(String msg) {
        System.out.println("Logged with MyLogger: " + msg);
        System.out.println("Times called: " + ++callCount);
    }
}
