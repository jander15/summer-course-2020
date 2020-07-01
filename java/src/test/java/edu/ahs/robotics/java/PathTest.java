package edu.ahs.robotics.java;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PathTest {

    @Test
    public void testDuplicatesRemoved() {
        // Make some points
        Point[] points = new Point[] {new Point(0,0),new Point(0,0), new Point(3,4), new Point(3,4), new Point(5,5), new Point(5,5)};
        Path path = new Path(points);


        Point[] pointsExpected = new Point[]{new Point(0, 0), new Point(3, 4), new Point(5, 5)};
        // Check to make sure that you got rid of the duplicates
        // Your code here!
        assertEquals(3,path.getWayPoints().size());

        assertEquals(pointsExpected[0],path.getWayPoints().get(0).point);
        assertEquals(0,path.getWayPoints().get(0).deltaXFromPrevious,.00001);
        assertEquals(0,path.getWayPoints().get(0).deltaYFromPrevious,.00001);
        assertEquals(0,path.getWayPoints().get(0).distanceFromPrevious,.00001);

        for (int i = 1; i <pointsExpected.length ; i++) {
            assertEquals(pointsExpected[i],path.getWayPoints().get(i).point);
            assertEquals(pointsExpected[i].x-pointsExpected[i-1].x,path.getWayPoints().get(i).deltaXFromPrevious,.00001);
            assertEquals(pointsExpected[i].y-pointsExpected[i-1].y,path.getWayPoints().get(i).deltaYFromPrevious,.00001);
            assertEquals(pointsExpected[i].distanceToPoint(pointsExpected[i-1]),path.getWayPoints().get(i).distanceFromPrevious,.00001);
        }
    }

    @Test
    public void totalDistance() {
        Point[] points = new Point[] {new Point(0,0), new Point(3,4), new Point(3,4), new Point(7,7), new Point(4,3)};
        Path path = new Path(points);
        assertEquals(15, path.totalDistance(), 0.00001);
    }


    @Test
    public void targetPoint() {
        Point[] points = new Point[] {new Point(0,0), new Point(0,3), new Point(1,3), new Point(2,3), new Point(8,3)};
        Point currentLocation = new Point(0.5,4);
        double distanceAlongPath = 6.0;

        Path path = new Path(points);

        Point actualPoint = path.targetPoint(currentLocation,distanceAlongPath).point;

        assertEquals(new Point(6.5,3),actualPoint);

    }
}