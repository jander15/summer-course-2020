package edu.ahs.robotics.java;

public class LineSegment {

    private Point point1;
    private Point point2;
    private double length;

    public LineSegment(Point point1, Point point2) {
        this.point1 = point1;
        this.point2 = point2;
        this.length = point1.distanceToPoint(point2);
    }

    public Point[] subDivide(int subSegments){
        Point[] points= new Point[subSegments-1];
        double xDiff = (point2.getX()-point1.getX())/subSegments;
        double yDiff = (point2.getY()-point1.getY())/subSegments;

        for( int i=1;i<subSegments;i++){
            Point p = new Point(point1.getX()+xDiff*i,point1.getY()+yDiff*i);
            points[i-1]=p;
        }

        return points;

    }

    public Point interpolate(double distanceFromFirstPoint){

        double deltaX=point2.getX()-point1.getX();
        double deltaY=point2.getY()-point1.getY();

        double scaleFactor=distanceFromFirstPoint/length;

        double scaledX=scaleFactor*deltaX;
        double scaledY=scaleFactor*deltaY;

        double finalX=point1.getX()+scaledX;
        double finalY=point1.getY()+scaledY;

        Point interpolatedPoint = new Point(finalX,finalY);

        return interpolatedPoint;
    }

    public double shortestDistance(Point current){
        double ux=point2.x-point1.x;
        double uy=point2.y-point1.y;

        double vx=current.x-point1.x;
        double vy=current.y-point1.y;

        double vMagnitude = point1.distanceToPoint(current);
        double uMagnitude = point2.distanceToPoint(point1);

        double dotProduct = ux*vx+uy*vy;
        double projection = dotProduct/uMagnitude;

        if(projection<0){
            return vMagnitude;
        }else if(projection>uMagnitude){
            return point2.distanceToPoint(current);
        }

        double distance = Math.sqrt(Math.pow(vMagnitude,2)-Math.pow(projection,2));
        return distance;
    }

}
