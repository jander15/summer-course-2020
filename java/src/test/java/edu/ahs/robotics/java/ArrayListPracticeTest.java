package edu.ahs.robotics.java;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ArrayListPracticeTest {

    @Test
    public void searchArrayListForString() {
        ArrayList<String> myArrayList = new ArrayList<>();
        myArrayList.add("hello");
        myArrayList.add("goodbye");
        myArrayList.add("Lars");
        myArrayList.add("Josh");

        String target = "Josh";

        assertEquals(true,ArrayListPractice.searchArrayListForString(myArrayList,target));
    }
}