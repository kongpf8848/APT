package org.example;

import org.example.annotation.Adder;
import org.example.MainGen;

public class Main {

    @Adder(num1 = 17, num2 = 25)
    public static void calculateSum() {

    }


    public static void main(String[] args) {
        System.out.println("Hello, World!");
        MainGen.calculateSum();

    }
}