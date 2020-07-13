package edu.ahs.robotics.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class GridLogger {

    private LogWriter writer;
    private Clock clock;
    private HashSet<String> categorySet;
    private HashMap<String,String> dataRow;
    private boolean firstRow = true;
    private ArrayList<String> categoryList;

    public GridLogger(LogWriter writer,Clock clock) {
        this.writer = writer;
        this.clock = clock;
        clock.reset();
        categorySet = new HashSet<>();
        dataRow = new HashMap<>();
        categoryList = new ArrayList<>();
        categoryList.add("Time");
    }

    /**
     * Add a value to the logger under the category.  Categories are lazily added to the logger
     * in the order encountered.
     * @param category
     * @param value
     */
    public void add(String category, String value) {
        if(firstRow && !categorySet.contains(category)) {
            //add the value to the HashSet and ArrayList
            categorySet.add(category);
            categoryList.add(category);
        }
        //Add the value to the HashMap
        dataRow.put(category, value);
    }

    /**
     * Write a line of data to the log.  If this is the first call to writeLn, categories are
     * written first, followed by the line of data.  Once the data is written, the logger is reset
     * and calls to add() will add values to the next line of data.
     */
    public void writeLn() {
        if(firstRow){
            //write the first row of categories
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < categoryList.size(); i++) {
                sb.append(categoryList.get(i));
                if(i<categoryList.size()-1) {
                    sb.append(",");
                }
            }
            writer.writeLine(sb.toString());
            firstRow = false;
        }
        //get time from clock and store in values Map
        dataRow.put("Time",Double.toString(clock.getCurrentTime()));
        //write the data
        StringBuffer newLine = new StringBuffer();
        for (int i = 0; i < categoryList.size() ; i++) {
            newLine.append(dataRow.get(categoryList.get(i)));
            //avoid putting comma at the end of the line
            if(i<categoryList.size()-1) {
                newLine.append(",");
            }
        }
        //write the new line to the LogWriter
        writer.writeLine(newLine.toString());

        //reset all values in the Hashmap to empty strings
            dataRow.clear();
    }

    public void stop() {}
}

