package edu.ahs.robotics.java;

import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GridLoggerTest {

    @Test
    public void writeLn() {
        TestWriter writer = new TestWriter();
        TestClock clock = new TestClock();
        GridLogger gridLogger = new GridLogger(writer,clock);
        gridLogger.add("RobotX", "2.4");
        gridLogger.add("RobotY","1.8");
        gridLogger.add("RobotX","1.5");
        clock.setCurrentTime(0);
        gridLogger.writeLn();
        gridLogger.add("RobotY","2.8");
        gridLogger.add("RobotX","2.5");
        clock.setCurrentTime(1);
        gridLogger.writeLn();
        gridLogger.add("RobotY","8.8");
        gridLogger.add("Unexpected","x");
        clock.setCurrentTime(2);
        gridLogger.writeLn();

        List lines = writer.getLines();
        // check the lines
         assertEquals("Time,RobotX,RobotY",lines.get(0));
         assertEquals("0.0,1.5,1.8",lines.get(1));
         assertEquals("1.0,2.5,2.8",lines.get(2));
         assertTrue(clock.resetCalled);
    }

    private class TestWriter implements LogWriter {

        List lines = new ArrayList();

        @Override
        public void writeLine(String line) {
            lines.add(line);
        }

        public List getLines() {
            return lines;
        }
    }

    private class TestClock implements Clock {
        private long currentTime;
        private boolean resetCalled=false;
        @Override

        public double getCurrentTime(){
            return currentTime;
        }
        public void reset(){
            resetCalled = true;
        }

        public void setCurrentTime(long time){
            currentTime=time;
        }

    }
}
