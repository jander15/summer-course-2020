package edu.ahs.robotics.java;

import java.util.ArrayList;

public class ArrayListPractice {

    public static void printArrayStrings(ArrayList<String> strings){
        for (int i = 0; i <strings.size(); i++) {
            System.out.println(strings.get(i));
        }
    }

    public static boolean searchArrayListForString(ArrayList<String> strings, String target){

        for (int i = 0; i <strings.size() ; i++) {

            if(target==strings.get(i)){
                return true;
            }
        }
        return false;
    }

}
