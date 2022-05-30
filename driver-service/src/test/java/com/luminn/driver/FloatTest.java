package com.luminn.driver;

public class FloatTest {
    public static void main(String[] ar){

        String m = "150.00";
        float v = Float.parseFloat(m);

        if(v == 0){
            System.out.println(" 1111 ");
        }
        else {
            System.out.println(" 2222 ");
        }
        System.out.println(v);

    }
}
