package edu.ahs.robotics.java;

import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.Line;

public class Path {

    private List<WayPoint> wayPoints;
    /**
     * @param rawPoints Array of X,Y points.  Consecutive duplicate points are discarded
     *                  A path must have at least 2 non-identical points
     */
    public Path(Point[] rawPoints) {
        wayPoints = new ArrayList<>();
        wayPoints.add(new WayPoint(rawPoints[0],0,0,0));

        for (int i = 1; i <rawPoints.length ; i++) {
            double deltaX = rawPoints[i].x-rawPoints[i-1].x;
            double deltaY = rawPoints[i].y-rawPoints[i-1].y;
            double distanceFromLast = rawPoints[i].distanceToPoint(rawPoints[i-1]);

            if(distanceFromLast!=0){
                wayPoints.add(new WayPoint(rawPoints[i],deltaX,deltaY,distanceFromLast));
            }

        }

    }

    /**
     * @return total distance of the path
     */
    public double totalDistance() {
        double distance = 0.0;
        for (int i = 0; i <wayPoints.size()-1 ; i++) {
            distance+= wayPoints.get(i+1).distanceFromPrevious;
        }

        return distance;
    }


    public List<WayPoint> getWayPoints() {
        return wayPoints;
    }

    /**
     * @return a point at the supplied distance along the path from the supplied current position
     * Note that the point will usually be interpolated between the points that originally defined the Path
     */
    public Path.WayPoint targetPoint(Point current, double distance) {
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
        double restOfCurrentSegment =wayPoints.get(i).componentAlongPath(current);
        double remainingDistance = distance-restOfCurrentSegment;
        while(remainingDistance>0){
                remainingDistance-=wayPoints.get(i).point.distanceToPoint(wayPoints.get(i+1).point);
                i++;
            }
        i--;

        remainingDistance+=wayPoints.get(i).point.distanceToPoint(wayPoints.get(i+1).point);
        Point first = wayPoints.get(i).point;
        Point second = wayPoints.get(i+1).point;
        Point point = new LineSegment(first,second).interpolate(remainingDistance);

        return new WayPoint(point,point.x-first.x,point.y-first.y,point.distanceToPoint(first));
    }


    public static class WayPoint {
        public Point point;
        public double deltaXFromPrevious;
        public double deltaYFromPrevious;
        public double distanceFromPrevious;

        private WayPoint(Point point, double deltaXFromPrevious, double deltaYFromPrevious, double distanceFromPrevious) {
            this.point = point;
            this.deltaXFromPrevious = deltaXFromPrevious;
            this.deltaYFromPrevious = deltaYFromPrevious;
            this.distanceFromPrevious = distanceFromPrevious;
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

            if (projection<0){
                return 0;
            }else if(projection>distanceFromPrevious){
                return distanceFromPrevious;
            }else {
                return projection;
            }
        }
    }



}
