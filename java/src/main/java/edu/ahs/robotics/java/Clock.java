package edu.ahs.robotics.java;

public interface Clock {
    /*
     * returns the time in milliseconds since last call to reset
     */
    double getCurrentTime();
    void reset();
}
