package com.stream.test;

import java.util.stream.Stream;

public class MyStream {
    public static void manin(String arg){
        System.out.println(" Test " );
        MyStream my = new MyStream();
        my.convertArraytoStream();
    }

    public void convertArraytoStream(){
        String[] languages = {"Java", "Python", "JavaScript"};
        Stream numbers = Stream.of(languages);
        numbers.forEach(System.out::println);

    }
}
