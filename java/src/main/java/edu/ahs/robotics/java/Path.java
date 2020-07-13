package edu.ahs.robotics.java;

import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.Line;

public class Path {

    private List<WayPoint> wayPoints;
    private double pathDistance;
    /**
     * @param rawPoints Array of X,Y points.  Consecutive duplicate points are discarded
     *                  A path must have at least 2 non-identical points
     */

    public Path(Point[] rawPoints) {
        if(rawPoints.length<2){
            throw new IllegalArgumentException("You need to send at least 2 Points to create a Path");
        }
        wayPoints = new ArrayList<>();
        wayPoints.add(new WayPoint(rawPoints[0],0,0,0,0));
        pathDistance=0;
        for (int i = 1; i <rawPoints.length ; i++) {
            double deltaX = rawPoints[i].x - rawPoints[i - 1].x;
            double deltaY = rawPoints[i].y - rawPoints[i - 1].y;
            double distanceFromLast = rawPoints[i].distanceToPoint(rawPoints[i - 1]);

            if (distanceFromLast > 0) {
                pathDistance += distanceFromLast;
                wayPoints.add(new WayPoint(rawPoints[i], deltaX, deltaY, distanceFromLast,pathDistance));
            }
        }
        if (wayPoints.size()<2){
            throw new IllegalArgumentException("You need to send at least 2 unique Points to create a Path");
        }
    }

    /**
     * @return total distance of the path AND
     */
    public double totalDistance() {
        return pathDistance;
    }

    public List<WayPoint> getWayPoints() {
        return wayPoints;
    }

    /**
     * @return a point at the supplied distance along the path from the supplied current position
     * Note that the point will usually be interpolated between the points that originally defined the Path
     */
    public WayPoint targetPoint(Point current, double targetDistance) {

        //calculate the closest Segment to the current position
        double shortestDistance = new LineSegment(wayPoints.get(0).point,wayPoints.get(1).point).shortestDistance(current);
        int shortestSegmentIndex=0;
        for (int i = 1; i <wayPoints.size()-1; i++) {
            LineSegment ls = new LineSegment(wayPoints.get(i).point,wayPoints.get(i+1).point);
            if(ls.shortestDistance(current)<=shortestDistance){
                shortestDistance=ls.shortestDistance(current);
                shortestSegmentIndex=i;
            }
        }

        int i=shortestSegmentIndex+1;
        /*
        while (wayPoints.get(i).componentAlongPath(current)<0) {
            i++;
        }*/

        //iterate through future segments to decide which WayPoints to interpolate and the distance to send to the interpolate method.
        double restOfCurrentSegment =wayPoints.get(i).componentAlongPath(current);
        double remainingDistance = targetDistance-restOfCurrentSegment;
        while(remainingDistance>0){
            if(i==wayPoints.size()-1){

                return wayPoints.get(i);
            }
                remainingDistance-=wayPoints.get(i).point.distanceToPoint(wayPoints.get(i+1).point);
                i++;
            }
        i--;
        remainingDistance+=wayPoints.get(i).point.distanceToPoint(wayPoints.get(i+1).point);
        Point first = wayPoints.get(i).point;
        Point second = wayPoints.get(i+1).point;
        Point point = new LineSegment(first,second).interpolate(remainingDistance);

        return new WayPoint(point,point.x-first.x,point.y-first.y,point.distanceToPoint(first),0);
    }

    public WayPoint targetPoint2(Point current, double targetDistance) {
        //1. find first wayPoint in front of current position
        int i =1;
        while(wayPoints.get(i).componentAlongPath(current)<=0 && i<wayPoints.size()-1)
        {
            i++;
        }
        //2. Find index of wayPoint in front of target point and remaining distance
        double remainingDistance = targetDistance-wayPoints.get(i).componentAlongPath(current);
        while(remainingDistance>=0 && i<wayPoints.size()-1){
            i++;
            remainingDistance-=wayPoints.get(i).distanceFromPrevious;
        }
        if(remainingDistance>0){
            return wayPoints.get(i);
        }

        remainingDistance+=wayPoints.get(i).distanceFromPrevious;


        //3. Interpolate
        Point a = wayPoints.get(i-1).point;
        Point b = wayPoints.get(i).point;
        Point targetPoint = new LineSegment(a,b).interpolate(remainingDistance);
        return new WayPoint(targetPoint,b.x-a.x, b.y-a.y,remainingDistance,0);
    }

    private double distanceToEndOfPath(Point current){
        //1. find first wayPoint in front of current position
        int i =1;
        while(wayPoints.get(i).componentAlongPath(current)<0 && i<wayPoints.size()-1)
        {
            i++;
        }
        //2. Find index of wayPoint in front of target point and remaining distance
        double distanceToEnd = wayPoints.get(i).componentAlongPath(current);
        while(i<wayPoints.size()-1){
            i++;
            distanceToEnd+=wayPoints.get(i).distanceFromPrevious;
        }
        return distanceToEnd;
    }

        public static class WayPoint {
        public Point point;
        public double deltaXFromPrevious;
        public double deltaYFromPrevious;
        public double distanceFromPrevious;
        public double distanceFromStart;

        private WayPoint(Point point, double deltaXFromPrevious, double deltaYFromPrevious, double distanceFromPrevious, double distanceFromStart) {
            this.point = point;
            this.deltaXFromPrevious = deltaXFromPrevious;
            this.deltaYFromPrevious = deltaYFromPrevious;
            this.distanceFromPrevious = distanceFromPrevious;
            this.distanceFromStart=distanceFromStart;


        }

        /**
         * Calculates the projection in the direction of the path from the current point to the
         * supplied WayPoint
         * @param current The source point
         * @return The dot product between vectors normalized by the length of the path segment leading to wayPoint
         */
        private double componentAlongPath(Point current) {
            double deltaXToWayPoint = point.x - current.x;
            double deltaYToWayPoint = point.y - current.y;

            double dotProduct = deltaXToWayPoint * deltaXFromPrevious + deltaYToWayPoint * deltaYFromPrevious;
            double projection = dotProduct / distanceFromPrevious;

           /* if (projection<0){
                return 0;
            }else if(projection>distanceFromPrevious){
                return distanceFromPrevious;
            }else {
                return projection;
            }

            */
           return projection;
        }
    }
}
