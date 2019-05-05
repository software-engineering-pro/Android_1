package com.example.coursetable;

import java.io.Serializable;

public class Event implements Serializable {
    private int eventCode;
    private String recordTime;
    private String topic;
    private String details;
    private String warnTime;
    private String relevantCourse;
    private String deadline;
    private int type_code;

    public Event(int eventCode, String recordTime, String topic, String details, String warnTime, String relevantCourse, String deadline, int type_code) {
        this.eventCode = eventCode;
        this.recordTime = recordTime;
        this.topic = topic;
        this.details = details;
        this.warnTime = warnTime;
        this.relevantCourse = relevantCourse;
        this.deadline = deadline;
        this.type_code = type_code;
    }

    public Event(){

    }

    public Event(int eventCode,String topic, String details, String deadline){
        this.eventCode = eventCode;
        this.topic = topic;
        this.details = details;
        this.deadline = deadline;
    }

    public int getEventCode() {
        return eventCode;
    }

    public void setEventCode(int eventCode) {
        this.eventCode = eventCode;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getWarnTime() {
        return warnTime;
    }

    public void setWarnTime(String warnTime) {
        this.warnTime = warnTime;
    }

    public String getRelevantCourse() {
        return relevantCourse;
    }

    public void setRelevantCourse(String relevantCourse) {
        this.relevantCourse = relevantCourse;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public int getType_code() {
        return type_code;
    }

    public void setType_code(int type_code) {
        this.type_code = type_code;
    }
}
