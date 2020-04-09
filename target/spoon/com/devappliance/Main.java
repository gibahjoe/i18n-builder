package com.devappliance;
public class Main {
    public static void main(java.lang.String[] args) {
        new spoon.Launcher().run(new java.lang.String[]{ "-i", "./src/main/java", "-o", "./target/spoon", "-p", "com.devappliance.processor.ConstructorProcessor" });
    }
}