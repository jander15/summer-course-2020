package edu.ahs.robotics.java;

import org.junit.Test;

import static org.junit.Assert.*;

public class LineSegmentTest {

    @Test
    public void subDivide() {
        Point[] pointsExpected = new Point[2];
        pointsExpected[0]=new Point(1,1); pointsExpected[1]= new Point(2,2);

        Point[] pointsActual = new LineSegment(new Point(0,0),new Point(3,3)).subDivide(3);

        for (int i = 0; i <pointsActual.length ; i++) {

            assertEquals(pointsExpected[i].getX(),pointsActual[i].getX(),0.000001);
            assertEquals(pointsExpected[i].getY(),pointsActual[i].getY(),0.000001);

        }
    }

    @Test
    public void interpolate() {
        LineSegment ls = new LineSegment(new Point(1,1),new Point(4,5));
        Point actual = ls.interpolate(7);
        assertEquals(new Point(5.2,6.6),actual);

    }

    @Test
    public void shortestDistance() {
        LineSegment ls =new LineSegment(new Point(1,1),new Point(4,4));
        assertEquals(1,ls.shortestDistance(new Point(5,4)),0.00001);
    }
}