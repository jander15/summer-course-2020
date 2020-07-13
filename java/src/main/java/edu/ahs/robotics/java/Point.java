package edu.ahs.robotics.java;

import java.util.Objects;

public class Point {
    public double x;
    public double y;


    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Math.abs(point.x-x) < 0.000001 &&
                Math.abs(point.y- y) < 0.000001;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public double distanceFromOrigin(){

        double distance = Math.pow(x*x+y*y,.5);

        return distance;
    }

    public String getQuadrant() {
        if (x > 0 && y > 0) {
            return "Quadrant 1";
        } else if (x<0 && y>0){
            return "Quadrant 2";
        } else if (x<0 && y<0){
            return "Quadrant 3";
        } else if (x>0 && y<0){
            return "Quadrant 4";
        } else if (x==0 || y==0){
            return "Axes";
        }
        else {
            return null;
        }
    }

    public double distanceToPoint(Point other){
        return Math.sqrt((Math.pow(other.x-x,2)+Math.pow(other.y-y,2)));
    }

    public Point closestPoint(Point[] points){
        Point closestPoint = points[0];

        for (int i = 1; i < points.length; i++) {
            double distance = this.distanceToPoint(points[i]);
            double distanceToClosestPoint = this.distanceToPoint(closestPoint);

            if(distance<distanceToClosestPoint){
                closestPoint = points[i];
            }
        }

        return closestPoint;
    }

    public static double distanceBetweenTwoPoints(Point a, Point b){
        return Math.sqrt(Math.pow(b.x-a.x,2)+Math.pow(b.y-a.y,2));
    }
}
